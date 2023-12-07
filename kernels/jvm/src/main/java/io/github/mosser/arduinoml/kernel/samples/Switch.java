package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.Arrays;

public class Switch {

	public static void scenario1(){
		// Declaring elementary bricks
		Sensor button = new Sensor();
		Sensor button2 = new Sensor();
		Sensor button3 = new Sensor();
		button.setName("button");
		button.setPin(9);
		button2.setName("button2");
		button2.setPin(10);
		button3.setName("button3");
		button3.setPin(11);

		Actuator led = new Actuator();
		led.setName("LED");
		led.setPin(12);

		ActuatorLCD lcd = new ActuatorLCD();
		lcd.setName("LCD");
		lcd.setBus(1);

		// Declaring states
		State on = new State();
		on.setName("on");

		State off = new State();
		off.setName("off");

		// Creating actions
		ActionSensor switchTheLightOn = new ActionSensor();
		switchTheLightOn.setActuator(led);
		switchTheLightOn.setValue(SIGNAL.HIGH);

		ActionSensor switchTheLightOff = new ActionSensor();
		switchTheLightOff.setActuator(led);
		switchTheLightOff.setValue(SIGNAL.LOW);

		ActionLCD displayText = new ActionLCD();
		displayText.setDisplayText(true);
		displayText.setActuatorLCD(lcd);

		ActionLCD clearText = new ActionLCD();
		displayText.setDisplayText(false);
		displayText.setActuatorLCD(lcd);

		// Binding actions to states
		on.setActions(Arrays.asList(switchTheLightOn));
		off.setActions(Arrays.asList(switchTheLightOff));

		on.setActions(Arrays.asList(displayText));
		off.setActions(Arrays.asList(clearText));

		// Creating transitions
		Transition on2off = new Transition();
		on2off.setNext(off);
		ConditionSensor cond1 = new ConditionSensor();
		cond1.setSensor(button);
		cond1.setValue(SIGNAL.HIGH);
		cond1.setOperator(OPERATOR.EMPTY);
		ConditionSensor cond2 = new ConditionSensor();
		cond2.setSensor(button2);
		cond2.setValue(SIGNAL.HIGH);
		cond2.setOperator(OPERATOR.AND);
		ConditionSensor cond4 = new ConditionSensor();
		cond4.setSensor(button3);
		cond4.setValue(SIGNAL.HIGH);
		cond4.setOperator(OPERATOR.OR);
		on2off.setConditions(Arrays.asList(cond1, cond2, cond4));

		Transition off2on = new Transition();
		off2on.setNext(on);
		ConditionSensor cond3 = new ConditionSensor();
		cond3.setSensor(button);
		cond3.setValue(SIGNAL.HIGH);
		cond3.setOperator(OPERATOR.EMPTY);
		off2on.setConditions(Arrays.asList(cond3));

		// Binding transitions to states
		on.setTransitions(Arrays.asList(on2off));
		off.setTransitions(Arrays.asList(off2on));

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Switch!");
		theSwitch.setBricks(Arrays.asList(button, button2, button3, led,lcd ));
		theSwitch.setStates(Arrays.asList(on, off));
		theSwitch.setInitial(off);

		// Generating Code
		Visitor codeGenerator = new ToWiring();
		theSwitch.accept(codeGenerator);

		// Printing the generated code on the console
		System.out.println(codeGenerator.getResult());
	}



	public static void scenario2(){
		// Declaring elementary bricks
		Sensor button = new Sensor();
		button.setName("button");
		button.setPin(9);

		Actuator led = new Actuator();
		led.setName("LED");
		led.setPin(11);

		ActuatorLCD lcd = new ActuatorLCD();
		lcd.setName("LCD");
		lcd.setBus(1);

		// Declaring states
		State on = new State();
		on.setName("on");

		State off = new State();
		off.setName("off");

		// Creating actions
		ActionSensor switchTheLightOn = new ActionSensor();
		switchTheLightOn.setActuator(led);
		switchTheLightOn.setValue(SIGNAL.HIGH);

		ActionSensor switchTheLightOff = new ActionSensor();
		switchTheLightOff.setActuator(led);
		switchTheLightOff.setValue(SIGNAL.LOW);

		ActionLCD displayText = new ActionLCD();
		displayText.setDisplayText(true);
		displayText.setActuatorLCD(lcd);

		ActionLCD clearText = new ActionLCD();
		clearText.setDisplayText(false);
		clearText.setActuatorLCD(lcd);

		// Binding actions to states
		on.setActions(Arrays.asList(switchTheLightOn));
		off.setActions(Arrays.asList(switchTheLightOff));

		on.setActions(Arrays.asList(displayText));
		off.setActions(Arrays.asList(clearText));

		// Creating transitions
		Transition on2off = new Transition();
		on2off.setNext(off);
		ConditionSensor cond1 = new ConditionSensor();
		cond1.setSensor(button);
		cond1.setValue(SIGNAL.HIGH);
		cond1.setOperator(OPERATOR.EMPTY);
		on2off.setConditions(Arrays.asList(cond1));


		Transition off2on = new Transition();
		off2on.setNext(on);
		ConditionSensor cond3 = new ConditionSensor();
		cond3.setSensor(button);
		cond3.setValue(SIGNAL.HIGH);
		cond3.setOperator(OPERATOR.EMPTY);
		off2on.setConditions(Arrays.asList(cond3));

		// Binding transitions to states
		on.setTransitions(Arrays.asList(on2off));
		off.setTransitions(Arrays.asList(off2on));

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Switch!");
		theSwitch.setBricks(Arrays.asList(button, led,lcd ));
		theSwitch.setStates(Arrays.asList(on, off));
		theSwitch.setInitial(off);

		// Generating Code
		Visitor codeGenerator = new ToWiring();
		theSwitch.accept(codeGenerator);

		// Printing the generated code on the console
		System.out.println(codeGenerator.getResult());
	}



	public static void main(String[] args) {
		scenario2();

	}

}
