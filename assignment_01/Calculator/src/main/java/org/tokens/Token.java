package org.tokens;

public class Token {
    private final Object value;

    public Token(Object value) {
        if (!(value instanceof Integer || value instanceof Double || value instanceof String)) {
            throw new IllegalArgumentException("Register can only hold int, double, or string.");
        }
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public Integer asInteger() {
        return (Integer) value;
    }

    public Double asDouble() {
        return (Double) value;
    }

    public String asString() {
        return (String) value;
    }

    public boolean isInteger() {
        return value instanceof Integer;
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

    public boolean isString() {
        return value instanceof String;
    }


    @Override
    public String toString() {
        return value.toString();
    }
}