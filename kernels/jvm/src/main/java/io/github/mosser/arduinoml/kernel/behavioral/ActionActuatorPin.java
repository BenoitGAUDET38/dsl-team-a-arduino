package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.ActuatorPin;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;

public class ActionActuatorPin extends Action {
    private SIGNAL value;
    private ActuatorPin actuator;

    public SIGNAL getOppositeValue(){
        if(value == SIGNAL.HIGH){
            return SIGNAL.LOW;
        }
        return SIGNAL.HIGH;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }

    public ActuatorPin getActuator() {
        return actuator;
    }

    public void setActuator(ActuatorPin actuator) {
        this.actuator = actuator;
    }

    @Override
    public String toString() {
        return "ActionActuatorPin{" +
                "value=" + value +
                ", actuator=" + actuator +
                '}';
    }
}
