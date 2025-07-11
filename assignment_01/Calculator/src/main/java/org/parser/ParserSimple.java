package org.parser;
//  Simplified version of Parser, better aligned with Calculator structure

import org.calculator.Calculator;
import org.registers.RegisterSet;

/**
 *  Class to help with parsing the language of the calculator.
 */
public class ParserSimple {
    private String input;
    private int m = 0; // operation mode
    Calculator calculator = new Calculator();
    RegisterSet registers = new RegisterSet();

    public ParserSimple(String input, Calculator calculator, RegisterSet registers) {
        this.input = input;
        this.calculator = calculator;
        this.registers = registers;
    }

    public void parseAll() throws ParseException {
        if (this.input.isEmpty()) {
            throw new ParseException("Empty input");
        }
        for (int pos = 0; pos < input.length(); pos++) {
            char currChar = input.charAt(pos);
            parseElement(currChar);
        }
    }

    /**
     * This is main method to parse tokens
     *
     * @throws ParseException
     */
    private void parseElement(char currChar) throws ParseException {
        // start with executable operation mode
        if (this.m == 0) {
            // case digits
            if (Character.isDigit(currChar)) {
                this.calculator.push(Character.getNumericValue(currChar)); // convert to integer and push on stack
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
                calculator.push(registers.read(currChar));
            }

            // case operators - pass supported symbols to Calculator
            else if ("=<>+-*/%&|_~?!$@\\#'".indexOf(currChar) >= 0) {
                calculator.executeCommand(currChar);
            }
        }

        // Integer Construction Mode
        else if (this.m == -1) {
            // case continue with digits
            if (Character.isDigit(currChar)) {
                addToInteger(currChar);
            } else if (currChar == '.') {
                convertToDecimal();
            } else {
                this.m = 0;
            }
        }

        // Decimal Place Construction Mode
        else if (this.m < -1) {
            if (Character.isDigit(currChar)) {
                addToDecimal(currChar); // update value on top of stack
                this.m -= 1;
            } else if (currChar == '.') {
                initializeDecimal();
            } else {
                this.m = 0; // back to execution mode
            }
        }

        // String Construction Mode
        else { // for this.m > 0
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
    }

    private void addToInteger(char currChar) {
        Object lastToken = this.calculator.pop();
        if (!(lastToken instanceof Integer)) {
            throw new RuntimeException("Expected IntegerToken on top of stack");
        }
        Integer value = ((Integer) lastToken) * 10 + Character.getNumericValue(currChar);;
        this.calculator.push(value); // push new Integer value on stack
    }

    private void initializeDecimal() {
        calculator.push(0.0); // push 0.0 on top of stack as decimal point
        this.m = -2; // enter Decimal Point Construction mode
    }

    private void convertToDecimal() {
        Object currToken = calculator.pop();
        calculator.push((Double) currToken); // convert and push back on top of stack as decimal point
        this.m = -2; // enter Decimal Point Construction mode
    }

    private void addToDecimal(char currChar) {
        Object lastToken = this.calculator.pop();
        if (!(lastToken instanceof Double)) {
            throw new RuntimeException("Expected Decimal Point on top of stack");
        }
        Double value = (Double) lastToken + Character.getNumericValue(currChar) * Math.pow(10, this.m + 1);
        calculator.push(value); // push new Double value (Decimal Point) on stack
        this.m -= 1; // stay in Decimal Place Construction mode, move decimal place
    }

    private void initializeString() {
        calculator.push(""); // push empty string to the top of stack as decimal point
        m = 1; // enter String Construction mode
    }

    private void addToString(char currChar) {
        Object lastToken = this.calculator.pop();
        if (!(lastToken instanceof String)) {
            throw new RuntimeException("Expected String on top of stack");
        }
        String updated = (String) lastToken + currChar;
        calculator.push(updated); // push new String statement on stack
    }
}
