package org.registers;

public class RegisterValue {
    private final Object value;

    public RegisterValue(Object value) {
        if (!(value instanceof Integer || value instanceof Double || value instanceof String)) {
            throw new IllegalArgumentException("Register can only hold int, double, or string.");
        }
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public int asInt() {
        return (int) value;
    }

    public double asDouble() {
        return (double) value;
    }

    public String asString() {
        return (String) value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}