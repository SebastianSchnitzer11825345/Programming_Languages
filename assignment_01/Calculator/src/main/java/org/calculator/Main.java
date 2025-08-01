package org.calculator;

import org.parser.ParseException;
import org.parser.Parser;

public class Main {
    public static void main(String[] args) throws ParseException {
        Context ctxt = new Context(); // this context has register 'a' contents as commandStream
        Calculator calculator = new Calculator(ctxt);
        run(calculator);
    }

    public static void run(Calculator calculator) throws ParseException {
        Parser parser = new Parser(calculator);
        parser.parseAll();

    }
}