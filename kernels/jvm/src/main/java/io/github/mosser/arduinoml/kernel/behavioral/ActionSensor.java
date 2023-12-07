package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.Actuator;
import io.github.mosser.arduinoml.kernel.structural.ActuatorBasic;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;

public class ActionSensor extends Action {
    private SIGNAL value;
    private ActuatorBasic actuator;

    public SIGNAL getValue() {
        return value;
    }
    public SIGNAL getOppositeValue(){
        if(value == SIGNAL.HIGH){
            return SIGNAL.LOW;
        }
        return SIGNAL.HIGH;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }

    public ActuatorBasic getActuator() {
        return actuator;
    }

    public void setActuator(ActuatorBasic actuator) {
        this.actuator = actuator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ActionSensor{" +
                "value=" + value +
                ", actuator=" + actuator +
                '}';
    }
}
