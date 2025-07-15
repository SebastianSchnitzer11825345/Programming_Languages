package org.calculator;

import org.parser.ParseException;
import org.parser.Parser;
import org.streams.*;

// TODO: I am not sure this is right
public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println("Hello, World!");
        Context ctxt = new Context();
        Calculator calculator = new Calculator(ctxt);

        run(calculator);
    }

    // TODO: running this function and writing 3 5 + " (Enter) does not print out anything...
    //  not sure why it is not yet working
    public static void run(Calculator calculator) throws ParseException {
        IStream input = calculator.getContext().getInputStream();
        calculator.getContext().addToCommandStreamInFront(input.readLine());
        Parser parser = new Parser(
                calculator.getContext().getCommandStream(),
                calculator);
        parser.parseAll();
    }
}