package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.ActuatorBus;

public class ActionLCD extends Action {

	private boolean displayText;
	private int rowNumber=0;
	private ActuatorBus actuatorBus;

	private String text;


	public boolean isDisplayText() {
		return displayText;
	}

	public void setDisplayText(boolean displayText) {
		this.displayText = displayText;
	}

	public ActuatorBus getActuatorLCD() {
		return actuatorBus;
	}

	public void setActuatorLCD(ActuatorBus actuatorBus) {
		this.actuatorBus = actuatorBus;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Action{" +
				"displayText=" + displayText +
				", actuator=" + actuatorBus +
				", text=" + text +
				", rowNumber=" + rowNumber +
				'}';
	}
}
