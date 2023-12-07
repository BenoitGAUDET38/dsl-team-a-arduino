package io.github.mosser.arduinoml.kernel.behavioral;

public class ConditionDelay extends SimpleCondition {
    private int delay;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        String delayString = Integer.toString(delay);
        return "ConditionDelay{" +
                "delay=" + delayString +
                '}';
    }
}
