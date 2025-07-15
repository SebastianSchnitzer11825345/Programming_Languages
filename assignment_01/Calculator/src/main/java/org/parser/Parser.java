package org.parser;

import org.calculator.Calculator;
import org.calculator.Context;
import org.registers.RegisterSet;

/**
 *  Class to help with parsing the language of the calculator.
 */
public class Parser {
    private final String commandStream;
    private int m = 0; // operation mode
    private Calculator calculator;
    private Context ctxt;
    private final RegisterSet registers;

    public Parser(String commandStream, Calculator calculator) {
        this.commandStream = commandStream;
        this.calculator = calculator;
        this.ctxt = calculator.getContext();
        this.registers = calculator.getContext().getRegisters();
    }

    /**
     *
     * Parse all elements in input (Command stack)
     * @throws ParseException
     */
    public void parseAll() throws ParseException {
        if (this.commandStream.isEmpty()) {
            throw new ParseException("Empty input");
        }
        for (int pos = 0; pos < this.commandStream.length(); pos++) {
            char currChar = this.commandStream.charAt(pos);
            parseElement(currChar);
        }
    }

    /**
     * Parse individual elements (each character)
     * @throws ParseException
     */
    private void parseElement(char currChar) throws ParseException {

        // start with executable operation mode
        if (this.m == 0) {
            runExecutionMode(currChar);
        }

        // Integer Construction Mode
        else if (this.m == -1) {
            runIntegerConstructionMode(currChar);
        }

        // Decimal Place Construction Mode
        else if (this.m < -1) {
            runDecimalPointConstructionMode(currChar);
        }

        // String Construction Mode
        else { // for this.m > 0
            runStringConstructionMode(currChar);
        }
    }


    private void runExecutionMode(char currChar) {
        // case digits
        if (Character.isDigit(currChar)) {
            this.ctxt.push(Character.getNumericValue(currChar)); // convert to integer and push on stack
            this.m = -1; // enter Integer Construction mode
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
            this.ctxt.push(this.registers.read(currChar));
        }

        // case operators - pass supported symbols to Calculator
        else if ("=<>+-*/%&|_~?!$@\\#'".indexOf(currChar) >= 0) {
            this.calculator.executeCommand(currChar);
        }
    }

    private void runIntegerConstructionMode (char currChar) {
        // case continue with digits
        if (Character.isDigit(currChar)) {
            addToInteger(currChar);
        } else if (currChar == '.') {
            convertToDecimal();
        } else {
            this.m = 0;
            runExecutionMode(currChar);
        }
    }

    public void runDecimalPointConstructionMode(char currChar) {
        if (Character.isDigit(currChar)) {
            addToDecimal(currChar); // update value on top of stack
            this.m -= 1;
        } else if (currChar == '.') {
            initializeDecimal();
        } else {
            this.m = 0; // back to execution mode
            runExecutionMode(currChar);
        }
    }

    public void runStringConstructionMode(char currChar) {
        // for this.m > 0
        // case String with "("
        if (currChar == ')') {
            this.m -= 1; // first decrement m
            if (this.m != 0) { // only add if m is not 0
                addToString(currChar);
            }
        }
        else if (currChar == '(') {
            addToString(currChar);
            this.m += 1;
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
        Object lastToken = this.ctxt.pop();
        if (!(lastToken instanceof Integer)) {
            throw new RuntimeException("Expected Integer on top of stack");
        }
        Integer value = ((Integer) lastToken) * 10 + Character.getNumericValue(currChar);;
        this.ctxt.push(value); // push new Integer value on stack
    }

    /**
     * Initialize Decimal point as Double, from either Execution mode or
     * one of Integer or Decimal Point Construction mode
     */
    private void initializeDecimal() {
        ctxt.push(0.0); // push 0.0 on top of stack as decimal point
        this.m = -2; // enter Decimal Point Construction mode
    }

    /**
     * Enter Decimal Point Construction mode from Integer Construction mode
     */
    private void convertToDecimal() {
        Object currToken = ctxt.pop();
        ctxt.push(((Number) currToken).doubleValue()); // convert and push back on top of stack as decimal point
        this.m = -2; // enter Decimal Point Construction mode
    }

    /**
     * Build Decimal Points in Decimal Points Construction Mode
     * @param currChar
     */
    private void addToDecimal(char currChar) {
        Object lastToken = this.ctxt.pop();
        if (!(lastToken instanceof Double)) {
            throw new RuntimeException("Expected Decimal Point on top of stack");
        }
        Double value = (double) lastToken + Character.getNumericValue(currChar) * Math.pow(10, this.m + 1);
        ctxt.push(value); // push new Double value (Decimal Point) on stack
        this.m -= 1; // stay in Decimal Place Construction mode, move decimal place
    }

    /**
     * Initialize String on stack with "", from either Execution mode or
     * String Construction mode
     */
    private void initializeString() {
        ctxt.push(""); // push empty string to the top of stack as decimal point
        m = 1; // enter String Construction mode
    }

    /**
     * Build String in String Construction Mode
     * @param currChar
     */
    private void addToString(char currChar) {
        Object lastToken = this.ctxt.pop();
        if (!(lastToken instanceof String)) {
            throw new RuntimeException("Expected String on top of stack");
        }
        String updated = (String) lastToken + currChar;
        ctxt.push(updated); // push new String statement on stack
    }
}
