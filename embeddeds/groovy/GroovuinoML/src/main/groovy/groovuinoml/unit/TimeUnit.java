package main.groovy.groovuinoml.unit;

public enum TimeUnit {
    millisecond("ms", 1.0),
    second("s", 1000.0),
    minute("min", 60000.0);

    String abbreviation;
    Double multiplier;
    TimeUnit(String abbreviation, Double multiplier) {
        this.abbreviation = abbreviation;
        this.multiplier = multiplier;
    }
    @Override
    public String toString() {
        return this.abbreviation;
    }
}
