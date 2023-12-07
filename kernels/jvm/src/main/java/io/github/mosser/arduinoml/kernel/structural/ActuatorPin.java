package io.github.mosser.arduinoml.kernel.structural;

import io.github.mosser.arduinoml.kernel.PinElement;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class ActuatorPin extends Actuator implements PinElement {
    private int pin;
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPin() {
        return pin;
    }

    @Override
    public void setPin(int pin) {
        this.pin = pin;
    }
}
