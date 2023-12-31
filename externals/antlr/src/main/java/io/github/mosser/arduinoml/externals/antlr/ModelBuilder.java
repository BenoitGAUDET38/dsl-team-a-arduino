package io.github.mosser.arduinoml.externals.antlr;

import io.github.mosser.arduinoml.externals.antlr.grammar.*;


import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder extends ArduinomlBaseListener {

    /********************
     ** Business Logic **
     ********************/

    private App theApp = null;
    private boolean built = false;

    public App retrieve() {
        if (built) { return theApp; }
        throw new RuntimeException("Cannot retrieve a model that was not created!");
    }

    /*******************
     ** Symbol tables **
     *******************/

    private Map<String, Sensor>   sensors   = new HashMap<>();
    private Map<String, ActuatorPin> basicActuators = new HashMap<>();
    private ActuatorBus actuatorBus;
    private Map<String, State>    states  = new HashMap<>();
    private Map<String, List<Binding>>  bindings  = new HashMap<>();

    private class Binding { // used to support state resolution for transitions
        String to;
        Condition condition;
        List<Action> actions;
    }

    private State currentState = null;

    /**************************
     ** Listening mechanisms **
     **************************/

    @Override
    public void enterRoot(ArduinomlParser.RootContext ctx) {
        built = false;
        theApp = new App();
    }

    @Override
    public void exitRoot(ArduinomlParser.RootContext ctx) {
        // Resolving states in transitions
        bindings.forEach((key, bindings) ->  {
            for (Binding binding : bindings){
                Transition t = new Transition();
                t.setNext(states.get(binding.to));
                t.setCondition(binding.condition);
                t.setActions(binding.actions);
                states.get(key).addTransition(t);
            }
        });
        this.built = true;
    }


    @Override
    public void enterDeclaration(ArduinomlParser.DeclarationContext ctx) {
        theApp.setName(ctx.name.getText());
    }

    @Override
    public void enterSensor(ArduinomlParser.SensorContext ctx) {
        Sensor sensor = new Sensor();
        sensor.setName(ctx.location().id.getText());
        sensor.setPin(Integer.parseInt(ctx.location().port.getText()));
        this.theApp.getBricks().add(sensor);
        sensors.put(sensor.getName(), sensor);
    }



    @Override
    public void enterActuator(ArduinomlParser.ActuatorContext ctx) {
        ActuatorPin actuator = new ActuatorPin();
        actuator.setName(ctx.location().id.getText());
        actuator.setPin(Integer.parseInt(ctx.location().port.getText()));
        this.theApp.getBricks().add(actuator);
        basicActuators.put(actuator.getName(), actuator);
    }
    @Override
    public void enterActuatorLCD(ArduinomlParser.ActuatorLCDContext ctx) {
        this.actuatorBus = new ActuatorBus();
        actuatorBus.setName(ctx.location().id.getText());
        actuatorBus.setBus(Integer.parseInt(ctx.location().port.getText()));
        this.theApp.getBricks().add(actuatorBus);
    }


    @Override
    public void enterState(ArduinomlParser.StateContext ctx) {
        State local = new State();
        local.setName(ctx.name.getText());
        this.currentState = local;
        this.states.put(local.getName(), local);
    }

    @Override
    public void exitState(ArduinomlParser.StateContext ctx) {
        this.theApp.getStates().add(this.currentState);
        this.currentState = null;
    }

    @Override
    public void enterActionSensor(ArduinomlParser.ActionSensorContext ctx) {
        ActionActuatorPin action = new ActionActuatorPin();
        action.setActuator(basicActuators.get(ctx.receiver.getText()));
        action.setValue(SIGNAL.valueOf(ctx.value.getText()));

        currentState.getActions().add(action);
    }
    @Override
    public void enterActionLCD(ArduinomlParser.ActionLCDContext ctx) {
        ActionLCD actionLCD = new ActionLCD();
        actionLCD.setActuatorBus(actuatorBus);
        actionLCD.setDisplayText(ctx.isDisplayed.getText().equals("TRUE"));
        if (ctx.isDisplayed.getText().equals("TRUE")){
            actionLCD.setText(ctx.text.getText().substring(1,ctx.text.getText().length()-1));
            if (ctx.rowNumber!=null)
                actionLCD.setRowNumber(Integer.parseInt(ctx.rowNumber.getText()));
        }
        currentState.getActions().add(actionLCD);
    }



    @Override
    public void enterTransition(ArduinomlParser.TransitionContext ctx) {
        // Creating a placeholder as the next state might not have been compiled yet.
        Binding toBeResolvedLater = new Binding();
        List<Action> actions = new ArrayList<>();

        toBeResolvedLater.to = ctx.next.getText();
        toBeResolvedLater.actions = actions;


        Condition firstCondition;

        if (ctx.time!=null){
            ConditionDelay condition= new ConditionDelay();
            condition.setDelay(Integer.parseInt(ctx.time.getText()));
            firstCondition = condition;
        }
        else{
            ConditionSensor condition= new ConditionSensor();
            condition.setSensor(sensors.get(ctx.trigger.getText()));
            condition.setValue(SIGNAL.valueOf(ctx.value.getText()));
            firstCondition = condition;
        }

        ArduinomlParser.ConditionContext conditionContext= ctx.more;

        if (conditionContext==null)
            toBeResolvedLater.condition = firstCondition;
        else{
            ComposedCondition conditionComposed = new ComposedCondition();
            toBeResolvedLater.condition = conditionComposed;
            conditionComposed.setLeft(firstCondition);
            while(conditionContext!=null){
                Condition conditionToAdd;
                conditionComposed.setOperator(OPERATOR.valueOf(conditionContext.operator.getText()));
                if (conditionContext.time!=null){
                    ConditionDelay condition = new ConditionDelay();
                    condition.setDelay(Integer.parseInt(conditionContext.time.getText()));
                    conditionToAdd = condition;
                }
                else{
                    ConditionSensor condition= new ConditionSensor();
                    condition.setSensor(sensors.get(conditionContext.trigger.getText()));
                    condition.setValue(SIGNAL.valueOf(conditionContext.value.getText()));
                    conditionToAdd=condition;
                }
                conditionContext=conditionContext.more;
                if (conditionContext!=null){
                    conditionComposed.setRight(new ComposedCondition());
                    conditionComposed= (ComposedCondition) conditionComposed.getRight();
                    conditionComposed.setLeft(conditionToAdd);
                }
                else
                    conditionComposed.setRight(conditionToAdd);
            }
        }


        ArduinomlParser.NewActionContext actionContext = ctx.mealy;
        while (actionContext != null){
            Action action;
            if (actionContext.receiverSensor!=null) {
                ActionActuatorPin actionSensor = new ActionActuatorPin();
                actionSensor.setActuator(basicActuators.get(actionContext.receiverSensor.getText()));
                actionSensor.setValue(SIGNAL.valueOf(actionContext.value.getText()));
                action = actionSensor;
            }
            else {
                ActionLCD actionLCD = new ActionLCD();
                actionLCD.setActuatorBus(actuatorBus);
                actionLCD.setDisplayText(actionContext.isDisplayed.getText().equals("TRUE"));
                if (actionContext.isDisplayed.getText().equals("TRUE")){
                    actionLCD.setText(actionContext.text.getText().substring(1,actionContext.text.getText().length()-1));
                    if (actionContext.rowNumber!=null)
                        actionLCD.setRowNumber(Integer.parseInt(actionContext.rowNumber.getText()));
                }
                action = actionLCD;
            }
            actions.add(action);
            actionContext = actionContext.mealy;
        }

        bindings.computeIfAbsent(currentState.getName(), k -> new ArrayList<>());
        bindings.get(currentState.getName()).add(toBeResolvedLater);
    }

    @Override
    public void enterInitial(ArduinomlParser.InitialContext ctx) {
        this.theApp.setInitial(this.currentState);
    }

}

