package io.github.mosser.arduinoml.kernel.generator;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {
	enum PASS {ONE, TWO, THREE}
	enum COND_PASS {ONE, TWO, THREE, FOUR}

	boolean withLCD = false;


	public ToWiring() {
		this.result = new StringBuffer();
	}

	private void w(String s) {
		result.append(String.format("%s",s));
	}

	@Override
	public void visit(App app) {
		//first pass, create global vars
		context.put("pass", PASS.ONE);
		w("// Wiring code generated from an ArduinoML model\n");
		w(String.format("// Application name: %s\n", app.getName())+"\n");

		w("long debounce = 200;\n");
		w("\nenum STATE {");
		String sep ="";
		for(State state: app.getStates()){
			w(sep);
			state.accept(this);
			sep=", ";
		}
		w("};\n");
		if (app.getInitial() != null) {
			w("STATE currentState = " + app.getInitial().getName()+";\n");
		}


		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		withLCD=app.getBricks().stream().anyMatch(brick -> brick instanceof ActuatorBus);

		//time since last state change
		w("\nlong timerSinceNewState = millis();\n");

		//second pass, setup and loop
		context.put("pass",PASS.TWO);
		w("\nvoid setup(){\n");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		w("}\n");

		w("\nvoid loop() {\n" +
			"\tswitch(currentState){\n");
		for(State state: app.getStates()){
			state.accept(this);
		}
		w("\t}\n" +
			"}");
	}

	@Override
	public void visit(ActuatorPin actuator) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]\n", actuator.getPin(), actuator.getName()));
		}
	}

	@Override
	public void visit(ActuatorBus actuatorBus) {
		if(context.get("pass") == PASS.ONE) {
			w("#include <LiquidCrystal.h>\n");
			switch (actuatorBus.getBus()) {
				case 1:
					w(String.format("LiquidCrystal lcd(2,3,4,5,6,7,8); // %s [LCD Actuator]\n", actuatorBus.getName()));
					break;
				case 2:
					w(String.format("LiquidCrystal lcd(10,11,12,13,14,15,16); // %s [LCD Actuator]\n", actuatorBus.getName()));
					break;
				default:
					break;
			}
		}
		if(context.get("pass") == PASS.TWO) {
			w(String.format("  lcd.begin(16,2); // %s [Actuator]\n", actuatorBus.getName()));
		}
	}

	@Override
	public void visit(Sensor sensor) {
		if(context.get("pass") == PASS.ONE) {
			w(String.format("\nboolean %sBounceGuard = false;\n", sensor.getName()));
			w(String.format("long %sLastDebounceTime = 0;\n", sensor.getName()));
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w(String.format("  pinMode(%d, INPUT);  // %s [Sensor]\n", sensor.getPin(), sensor.getName()));
		}
	}

	@Override
	public void visit(State state) {
		if(context.get("pass") == PASS.ONE){
			w(state.getName());
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w("\t\tcase " + state.getName() + ":\n");
			for (Action action : state.getActions()) {
				action.accept(this);
			}

			if (state.getTransitions() != null) {
				for(Transition transition : state.getTransitions()) {
					transition.accept(this);
				}
				w("\t\tbreak;\n");
			}
		}

	}

	@Override
	public void visit(Transition transition) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			context.put("cond_pass", COND_PASS.ONE);
			transition.getCondition().accept(this);
			w(String.format("\t\t\tif ("));
			context.put("cond_pass", COND_PASS.TWO);
			transition.getCondition().accept(this);
			context.put("cond_pass", COND_PASS.THREE);
			transition.getCondition().accept(this);
			w(String.format(") {\n"));
			context.put("cond_pass", COND_PASS.FOUR);
			transition.getCondition().accept(this);
			context.put("pass", PASS.THREE);
			for (Action action : transition.getActions()) {
				action.accept(this);
			}
			context.put("pass", PASS.TWO);
			w("\t\t\t\ttimerSinceNewState = millis();\n");
			w("\t\t\t\tcurrentState = " + transition.getNext().getName() + ";\n");
			if (withLCD) {
				w("\t\t\t\tlcd.clear();\n");
			}
			w("\t\t\t}\n");
        }
	}

	@Override
	public void visit(ComposedCondition condition) {
		condition.getLeft().accept(this);
		if(context.get("cond_pass") == COND_PASS.TWO) {
			if (condition.getOperator() == OPERATOR.AND) {
				w(" && ");
			} else if (condition.getOperator() == OPERATOR.OR) {
				w(" || ");
			}
		}
		condition.getRight().accept(this);
	}

	@Override
	public void visit(SimpleCondition condition) {
		if(context.get("cond_pass") == COND_PASS.ONE && condition instanceof ConditionSensor) {
			ConditionSensor conditionSensor = (ConditionSensor) condition;
			String sensorName = conditionSensor.getSensor().getName();
			w(String.format("\t\t\t%sBounceGuard = millis() - %sLastDebounceTime > debounce;\n",
					sensorName, sensorName));
		}
		if(context.get("cond_pass") == COND_PASS.TWO) {
			String stringToAdd="";
			if (condition instanceof ConditionSensor) {
				ConditionSensor conditionSensor = (ConditionSensor) condition;
				stringToAdd=String.format("digitalRead(%d) == %s", conditionSensor.getSensor().getPin(), conditionSensor.getValue());
			}
			else if (condition instanceof ConditionDelay) {
				ConditionDelay conditionDelay = (ConditionDelay) condition;
				stringToAdd=String.format("millis() - timerSinceNewState > %d", conditionDelay.getDelay());
			}
			w(stringToAdd);
		}
		if(context.get("cond_pass") == COND_PASS.THREE  && condition instanceof ConditionSensor ) {
			ConditionSensor conditionSensor = (ConditionSensor) condition;
			String sensorName = conditionSensor.getSensor().getName();
			w(String.format(" && %sBounceGuard", sensorName));
		}
		if(context.get("cond_pass") == COND_PASS.FOUR  && condition instanceof ConditionSensor ) {
			ConditionSensor conditionSensor = (ConditionSensor) condition;
			String sensorName = conditionSensor.getSensor().getName();
			w(String.format("\t\t\t\t%sLastDebounceTime = millis();\n", sensorName));
		}
	}




	@Override
	public void visit(ActionActuatorPin actionSensor) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w(String.format("\t\t\tdigitalWrite(%d,%s);\n", actionSensor.getActuator().getPin(), actionSensor.getValue()));
		}
		if (context.get("pass") == PASS.THREE) {
			w(String.format("\t\t\t\tdigitalWrite(%d,%s);\n", actionSensor.getActuator().getPin(), actionSensor.getValue()));
		}
	}

	@Override
	public void visit(ActionLCD actionLCD) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			if (actionLCD.isDisplayText()) {
				w(String.format("\t\t\tlcd.setCursor(0,%d);\n\t\t\tlcd.print(\"%s\");\n", actionLCD.getRowNumber(),actionLCD.getText()));
			} else {
				w("\t\t\tlcd.clear();\n");
			}
		}
	}

}
