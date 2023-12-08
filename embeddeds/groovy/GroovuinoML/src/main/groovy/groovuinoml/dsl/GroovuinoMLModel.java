package main.groovy.groovuinoml.dsl;

import java.util.*;

import groovy.lang.Binding;
import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;

public class GroovuinoMLModel {
	private List<Brick> bricks;
	private List<State> states;
	private State initialState;
	
	private Binding binding;
	
	public GroovuinoMLModel(Binding binding) {
		this.bricks = new ArrayList<Brick>();
		this.states = new ArrayList<State>();
		this.binding = binding;
	}
	
	public void createSensor(String name, Integer pinNumber) {
		Sensor sensor = new Sensor();
		sensor.setName(name);
		sensor.setPin(pinNumber);
		this.bricks.add(sensor);
		this.binding.setVariable(name, sensor);
	}
	
	public void createActuatorPin(String name, Integer pinNumber) {
		ActuatorPin actuatorPin = new ActuatorPin();
		actuatorPin.setName(name);
		actuatorPin.setPin(pinNumber);
		this.bricks.add(actuatorPin);
		this.binding.setVariable(name, actuatorPin);
	}

	public void createActuatorBus(String name, Integer busNumber) {
		ActuatorBus actuatorBus = new ActuatorBus();
		actuatorBus.setName(name);
		actuatorBus.setBus(busNumber);
		this.bricks.add(actuatorBus);
		this.binding.setVariable(name, actuatorBus);
	}
	
	public void createState(String name, List<Action> actions) {
		State state = new State();
		state.setName(name);
		state.setActions(actions);
		this.states.add(state);
		this.binding.setVariable(name, state);
	}
	
	public Transition createTransition(State from, State to, ComposedCondition condition, List<Action> actions) {
		Transition transition = new Transition();
		transition.setNext(to);
		if (condition.getRight() == null) {
			transition.setCondition(condition.getLeft());
		} else {
			transition.setCondition(condition);
		}
		transition.setActions(actions);
		from.addTransition(transition);
		return transition;
	}
	
	public void setInitialState(State state) {
		this.initialState = state;
	}
	
	@SuppressWarnings("rawtypes")
	public Object generateCode(String appName) {
		App app = new App();
		app.setName(appName);
		app.setBricks(this.bricks);
		app.setStates(this.states);
		app.setInitial(this.initialState);
		Visitor codeGenerator = new ToWiring();
		app.accept(codeGenerator);
		
		return codeGenerator.getResult();
	}
}
