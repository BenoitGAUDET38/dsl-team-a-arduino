package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.ActuatorLCD;

public class ActionLCD extends Action {

	private boolean displayText;
	private int rowNumber=0;
	private ActuatorLCD actuatorLCD;

	private String text;


	public boolean isDisplayText() {
		return displayText;
	}

	public void setDisplayText(boolean displayText) {
		this.displayText = displayText;
	}

	public ActuatorLCD getActuatorLCD() {
		return actuatorLCD;
	}

	public void setActuatorLCD(ActuatorLCD actuatorLCD) {
		this.actuatorLCD = actuatorLCD;
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
				", actuator=" + actuatorLCD +
				", text=" + text +
				", rowNumber=" + rowNumber +
				'}';
	}
}
