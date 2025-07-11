package org.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.calculator.Context;
import org.registers.RegisterSet;
import org.tokens.*;

/**
 * Class to help with parsing the language of the calculator.
 */
public class Parser {
    private String input;
    private int m = 0; // operation mode

    // TODO: fix the method to our needs
    /**
     * This is main method to parse tokens
     * @param input
     * @return
     * @throws ParseException
     */
    public Context parse(String input) throws ParseException {
        Context context = new Context();

        if (mismatchedBrackets()) {
            throw new MismatchedBracketsException();
        }

        if (input.isEmpty()) {
            throw new ParseException("Empty input");
        }
        int pos = 0;
        char currChar = input.charAt(pos);

        // start with executable operation mode
        if (m == 0) {
            // case digits
            if (Character.isDigit(currChar)) {
                IntegerToken currToken = new IntegerToken((int) currChar);
                currToken.apply(context);
                m = -1;
            }

            // case dot
            else if (currChar == '.') {
                DecimalPointToken currToken = new DecimalPointToken(0.0);
                currToken.apply(context); // push 0.0 back on top of stack as decimal point
                m = -2; // enter Decimal Point Construction mode
            }

            // case String with "("
            else if (currChar == '(') {
                StringToken currToken = new StringToken("");
                currToken.apply(context); // push empty string to the top of stack as decimal point
                m = 1; // enter String Construction mode
            }

            // case Letter (call register)
            else if (Character.isLetter(currChar)) {
                IToken currRegister = context.getRegisters().read(currChar);

            }

            // case operators - move to Calculator
            else if (currChar is Command){
            }
        }

                // Integer Construction Mode
        if (m == -1) {
            // case continue with digits
            if (Character.isDigit(currChar)) {
                int digit = Character.getNumericValue(currChar);
                IToken lastToken = context.getDataStack().pop();
                if (!(lastToken instanceof IntegerToken)) {
                    throw new RuntimeException("Expected IntegerToken on top of stack");
                }
                ((IntegerToken) lastToken).construct(context, digit); // update value on top of stack

            // case entering Decimal Point Construction Mode
            } else if (currChar == '.') {
                IToken currToken = context.getDataStack().pop();
                ((DecimalPointToken) currToken).apply(context); // convert and push back on top of stack as decimal point
                m = -2; // enter Decimal Point Construction mode
            }
            else {
                m = 0;
            }
        }

        // Decimal Place Construction Mode
        if (m < -1) {
            if (Character.isDigit(currChar)) {
                int digit = Character.getNumericValue(currChar);
                IToken lastToken = context.getDataStack().pop();
                if (!(lastToken instanceof DecimalPointToken)) {
                    throw new RuntimeException("Expected Decimal Point on top of stack");
                }
                ((DecimalPointToken) lastToken).construct(context, digit, m); // update value on top of stack
                m -= 1; // stay in Decimal Place Construction mode
            } else if (currChar == '.') {
                DecimalPointToken currToken = new DecimalPointToken(0.0);
                currToken.apply(context); // push 0.0 on top of stack as decimal point
                m = -2; // enter Decimal Point Construction mode
            } else {
                m = 0; // back to execution mode
            }
        }

        // String Construction Mode
        if (m > 0) {




        }
        return context;
    }

    private List<IToken> parseTokens(String input) throws ParseException {
        this.input = input;
        List<IToken> tokens = new ArrayList<>();
        IToken token;

        while ((token = nextToken()) != null) {
            tokens.add(token);
        }

        return tokens;
    }

    private IToken nextToken() throws ParseException {
        // remove whitespace
        input = input.trim();

        if (input.isEmpty())
            return null;

        char ch = input.charAt(0);
        if (Character.isDigit(ch)) {
            return nextInt();
        } else if (ch == '(') {
            return nextBlock();
        } else {
            return nextOperator();
        }
    }

    private IntegerToken nextInt() {
        int val = 0;
        while (!input.isEmpty() && Character.isDigit(input.charAt(0))) {
            val *= 10;
            val += Character.getNumericValue(input.charAt(0));
            input = input.substring(1);
        }

        return new IntegerToken(val);
    }


    private StringToken nextString()  {

        // parse block content
        String inner = input.substring(1, i);

        StringToken statement = new StringToken();

        Parser p = new Parser();
        block.getTokens().addAll(p.parseTokens(inner));

        input = input.substring(i+1);
        return block;
    }

    // TODO: check which operators needed here and implement
    private IToken nextOperator() throws ParseException {
        char ch = input.charAt(0);
        input = input.substring(1);
        IToken operator = OperatorFactory.getOperator(ch);

        if (operator == null) {
            throw new ParseException("Unknown operator: " + ch);
        }

        return operator;
    }

    // TODO: already adjusted
    private BlockElement nextBlock() throws ParseException {
        // find matching bracket
        int openBrackets = 1;
        int i;
        for (i = 1; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                openBrackets++;
            } else if (input.charAt(i) == ')') {
                openBrackets--;
            }
            if (openBrackets == 0) break;
        }

        // parse block content
        String inner = input.substring(1, i);
        BlockElement block = new BlockElement();

        Parser p = new Parser();
        block.getTokens().addAll(p.parseTokens(inner));

        input = input.substring(i+1);
        return block;
    }

    /**
     * Check for mismatched brackets
     * @return <tt>true</tt> if there are mismatched brackets,
     *         <tt>false</tt> otherwise
     */
    private boolean mismatchedBrackets() {
        int openBrackets = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '[') {
                openBrackets++;
            } else if (input.charAt(i) == ']') {
                openBrackets--;
            }
            if (openBrackets < 0) return true;
        }

        return openBrackets > 0;
    }

}
