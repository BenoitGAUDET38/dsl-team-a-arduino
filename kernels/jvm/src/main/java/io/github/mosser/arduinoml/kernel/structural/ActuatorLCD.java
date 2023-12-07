package io.github.mosser.arduinoml.kernel.structural;

import io.github.mosser.arduinoml.kernel.BusElement;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class ActuatorLCD extends Brick implements BusElement {
	private int bus;

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int getBus() {
		return bus;
	}

	@Override
	public void setBus(int bus) {
		this.bus = bus;
	}
}
