package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.Actuator;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.Sensor;

import java.util.Arrays;

public class MealyMachineSample {

	public static void main(String[] args) {

		// Declaring elementary bricks
		Sensor button1 = new Sensor();
		Sensor button2 = new Sensor();
		Sensor button3 = new Sensor();
		button1.setName("button1");
		button1.setPin(8);
		button2.setName("button2");
		button2.setPin(9);
		button3.setName("button3");
		button3.setPin(10);

		Actuator led1 = new Actuator();
		Actuator led2 = new Actuator();
		led1.setName("led1");
		led2.setName("led2");;
		led1.setPin(11);
		led2.setPin(12);

		// Declaring states
		State on = new State();
		on.setName("on");

		State error = new State();
		error.setName("error");

		// Creating actions
		Action led1On = new Action();
		led1On.setActuator(led1);
		led1On.setValue(SIGNAL.HIGH);

		Action led1Off = new Action();
		led1Off.setActuator(led1);
		led1Off.setValue(SIGNAL.LOW);

		Action led2On = new Action();
		led2On.setActuator(led2);
		led2On.setValue(SIGNAL.HIGH);

		Action led2Off = new Action();
		led2Off.setActuator(led2);
		led2Off.setValue(SIGNAL.LOW);

		on.setActions(Arrays.asList(led1Off, led2Off));

		// Creating transitions
		Transition onLed1_offLed2 = new Transition();
		onLed1_offLed2.setNext(error);
		Condition cond1 = new Condition();
		cond1.setSensor(button1);
		cond1.setValue(SIGNAL.HIGH);
		cond1.setOperator(OPERATOR.EMPTY);
		onLed1_offLed2.setConditions(Arrays.asList(cond1));
		onLed1_offLed2.setActions(Arrays.asList(led1On, led2Off));

		Transition offLed1_onLed2 = new Transition();
		offLed1_onLed2.setNext(error);
		Condition cond2 = new Condition();
		cond2.setSensor(button2);
		cond2.setValue(SIGNAL.HIGH);
		cond2.setOperator(OPERATOR.EMPTY);
		offLed1_onLed2.setConditions(Arrays.asList(cond2));
		offLed1_onLed2.setActions(Arrays.asList(led1Off, led2On));

		Transition onLed1_onLed2 = new Transition();
		onLed1_onLed2.setNext(error);
		Condition cond3 = new Condition();
		cond3.setSensor(button3);
		cond3.setValue(SIGNAL.HIGH);
		cond3.setOperator(OPERATOR.EMPTY);
		onLed1_onLed2.setConditions(Arrays.asList(cond3));
		onLed1_onLed2.setActions(Arrays.asList(led1On, led2On));

		// Binding transitions to states
		on.setTransitions(Arrays.asList(onLed1_offLed2, offLed1_onLed2, onLed1_onLed2));

		// Building the App
		App mealySample = new App();
		mealySample.setName("Mealy Machine");
		mealySample.setBricks(Arrays.asList(button1, button2, button3, led1, led2));
		mealySample.setStates(Arrays.asList(on, error));
		mealySample.setInitial(on);

		// Generating Code
		Visitor codeGenerator = new ToWiring();
		mealySample.accept(codeGenerator);

		// Printing the generated code on the console
		System.out.println(codeGenerator.getResult());
	}

}
