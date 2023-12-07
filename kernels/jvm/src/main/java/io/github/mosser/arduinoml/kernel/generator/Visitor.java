package io.github.mosser.arduinoml.kernel.generator;

import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;
import io.github.mosser.arduinoml.kernel.App;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);
	public abstract void visit(Transition transition);

	public abstract void visit(ComposedCondition condition);
	public abstract void visit(SimpleCondition condition);

	public abstract void visit(ActionLCD action);
	public abstract void visit(ActionSensor action);

	public abstract void visit(Actuator actuator);
	public abstract void visit(Sensor sensor);
	public abstract void visit(ActuatorLCD actuatorLCD);


	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

}

