package org.calculator;

public class Main {
    public static void main(String[] args) {
        Context ctxt = new Context(); // this context has register 'a' contents as commandStream
        Calculator calculator = new Calculator(ctxt);
        run(calculator);
    }

    public static void run(Calculator calculator) {
        calculator.run();
    }
}