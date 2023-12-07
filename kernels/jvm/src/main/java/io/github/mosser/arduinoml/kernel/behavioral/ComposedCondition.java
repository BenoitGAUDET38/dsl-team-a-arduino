package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class ComposedCondition extends Condition{
    private Condition left;
    private Condition right;
    private OPERATOR operator;

    public Condition getLeft() {
        return left;
    }

    public void setLeft(Condition left) {
        this.left = left;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Condition getRight() {
        return right;
    }

    public void setRight(Condition right) {
        this.right = right;
    }

    public OPERATOR getOperator() {
        return operator;
    }

    public void setOperator(OPERATOR operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "ComposedCondition{" +
                "left=" + left +
                ", right=" + right +
                ", operator=" + operator +
                '}';
    }
}
