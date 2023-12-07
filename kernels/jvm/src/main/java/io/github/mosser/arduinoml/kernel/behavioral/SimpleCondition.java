package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public abstract class SimpleCondition extends Condition{
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
