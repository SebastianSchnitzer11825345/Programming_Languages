package org.example;

import java.io.Serial;

public class CalculatorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CalculatorException() {
        super();
    }

    public CalculatorException(String msg) {
        super(msg);
    }
}