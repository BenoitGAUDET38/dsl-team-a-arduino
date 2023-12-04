package io.github.mosser.arduinoml.kernel.behavioral;

public class ActionHold extends Action{

    private int duration;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
