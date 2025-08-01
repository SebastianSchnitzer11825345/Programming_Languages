package org.calculator;

import org.parser.ParseException;
import org.parser.Parser;
import org.streams.*;

// TODO: I am not sure this is right
public class Main {
    public static void main(String[] args) throws ParseException {
        Context ctxt = new Context(); // this context has register 'a' contents as commandStream
        Calculator calculator = new Calculator(ctxt);

        run(calculator);
    }

    // TODO: running this function and writing 3 5 + " (Enter) does not print out anything...
    //  not sure why it is not yet working
    public static void run(Calculator calculator) throws ParseException {
//        IStream input = calculator.getContext().getInputStream();
//        calculator.getContext().addToCommandStreamInFront(input.readLine());
        Parser parser = new Parser(calculator);
        parser.parseAll();

        //TODO: This is more of a workaround than an actual solution to your problem, but for now it gets the job done

            while (calculator.size() > 0) {
                System.out.println(calculator.pop());
            }
    }
}