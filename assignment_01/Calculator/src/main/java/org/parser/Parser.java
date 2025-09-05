package org.parser;

import org.calculator.Calculator;

import java.util.Objects;

/**
 *  Class to help with parsing the language of the calculator.
 */
public class Parser {
    private final Calculator calculator;
    private int m = 0; // operation mode

    public Parser(Calculator calculator) {
        this.calculator = calculator;
    }

    /**
     *
     * Parse all elements in input (Command stack)
     * @throws ParseException
     */
    public void parseAll() throws ParseException {
        if (calculator.getContext().getCommandStream().isEmpty()) {
            throw new ParseException("Empty input");
        }

        while (!calculator.getContext().getCommandStream().isEmpty()) {
            char currChar = calculator.getContext().getCommandStream().charAt(0);
            calculator.getContext().removeExecCharFromCommandStream();
            try {
                parseElement(currChar);
            } catch (Exception e) {
                System.err.println("Error executing command " + currChar + ": " + e.getMessage());
                break;
            }
//            // TODO(Alenka): remove when all is working (all tests finished first)
            System.out.println("Parse-State:" + calculator.getContext().toString());
//            if (calculator.getContext().getCommandStream().length() > 2) {
//                if (Objects.equals(calculator.getContext().getCommandStream().substring(0, 2), "A@")) {
//                    System.out.println("Parse-State:" + calculator.getContext().toString());
//                }
//            }
        }
    }

    /**
     * Parse individual elements (each character)
     * @throws ParseException
     */
    private void parseElement(char currChar) throws ParseException {

        // start with executable operation mode
        if (m == 0) {
            runExecutionMode(currChar);
        }

        // Integer Construction Mode
        else if (m == -1) {
            runIntegerConstructionMode(currChar);
        }

        // Decimal Place Construction Mode
        else if (m < -1) {
            runDecimalPointConstructionMode(currChar);
        }

        // String Construction Mode
        else { // for m > 0
            runStringConstructionMode(currChar);
        }
    }


    private void runExecutionMode(char currChar) {
        // case digits
        if (Character.isDigit(currChar)) {
            calculator.getContext().push(Character.getNumericValue(currChar)); // convert to integer and push on stack
            m = -1; // enter Integer Construction mode
        }

        // case dot
        else if (currChar == '.') {
            initializeDecimal();
        }

        // case String with "("
        else if (currChar == '(') {
            initializeString();
        }

        // case Letter (call register and push its contents to stack)
        else if (Character.isLetter(currChar)) {
            calculator.getContext().push(calculator.getContext().getRegisters().read(currChar));
        }

        // case operators - pass supported symbols to Calculator
        else if ("+-*/%&|_~?!$@\\#'\"=<>".indexOf(currChar) >= 0) {
            calculator.executeCommand(currChar);
        }
        // Everything else in the command stream does nothing (except of
        // separating two adjacent numbers from each other). A string can be
        // used as plain text if neither ’@’ nor \ is applied to it.
        else {
            if (calculator.getContext().isTestMode()) {
                System.out.println("Ignored character: " + currChar);
            }
        }
    }

    private void runIntegerConstructionMode (char currChar) {
        // case continue with digits
        if (Character.isDigit(currChar)) {
            addToInteger(currChar);
        } else if (currChar == '.') {
            convertToDecimal();
        } else {
            m = 0;
            runExecutionMode(currChar);
        }
    }

    public void runDecimalPointConstructionMode(char currChar) {
        if (Character.isDigit(currChar)) {
            addToDecimal(currChar); // update value on top of stack
            m -= 1;
        } else if (currChar == '.') {
            initializeDecimal();
        } else {
            m = 0; // back to execution mode
            runExecutionMode(currChar);
        }
    }

    public void runStringConstructionMode(char currChar) {
        // for m > 0
        // case String with "("
        if (currChar == ')') {
            m -= 1; // first decrement m
            if (m != 0) { // only add if m is not 0
                addToString(currChar);
            }
        }
        else if (currChar == '(') {
            addToString(currChar);
            m += 1;
        }
        else {
            addToString(currChar); // no change to m
        }
    }


    /**
     * Build integers in Integer Construction Mode
     * @param currChar
     */
    private void addToInteger(char currChar) {
        Object lastToken = calculator.getContext().pop();
        if (!(lastToken instanceof Integer)) {
            throw new RuntimeException("Expected Integer on top of stack");
        }
        Integer value = ((Integer) lastToken) * 10 + Character.getNumericValue(currChar);
        calculator.getContext().push(value); // push new Integer value on stack
    }

    /**
     * Initialize Decimal point as Double, from either Execution mode or
     * one of Integer or Decimal Point Construction mode
     */
    private void initializeDecimal() {
        calculator.getContext().push(0.0); // push 0.0 on top of stack as decimal point
        m = -2; // enter Decimal Point Construction mode
    }

    /**
     * Enter Decimal Point Construction mode from Integer Construction mode
     */
    private void convertToDecimal() {
        Object currToken = calculator.getContext().pop();
        calculator.getContext().push(((Number) currToken).doubleValue()); // convert and push back on top of stack as decimal point
        m = -2; // enter Decimal Point Construction mode
    }

    /**
     * Build Decimal Points in Decimal Points Construction Mode
     * @param currChar
     */
    private void addToDecimal(char currChar) {
        Object lastToken = calculator.getContext().pop();
        if (!(lastToken instanceof Double)) {
            throw new RuntimeException("Expected Decimal Point on top of stack");
        }
        Double value = (double) lastToken + Character.getNumericValue(currChar) * Math.pow(10, m + 1);
        calculator.getContext().push(value); // push new Double value (Decimal Point) on stack
        m -= 1; // stay in Decimal Place Construction mode, move decimal place
    }

    /**
     * Initialize String on stack with "", from either Execution mode or
     * String Construction mode
     */
    private void initializeString() {
        calculator.getContext().push(""); // push empty string to the top of stack as decimal point
        m = 1; // enter String Construction mode
    }

    /**
     * Build String in String Construction Mode
     * @param currChar
     */
    private void addToString(char currChar) {
        Object lastToken = calculator.getContext().pop();
        if (!(lastToken instanceof String)) {
            throw new RuntimeException("Expected String on top of stack");
        }
        String updated = (String) lastToken + currChar;
        calculator.getContext().push(updated); // push new String statement on stack
    }
}
