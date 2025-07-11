package org.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.calculator.Context;
import org.tokens.*;

/**
 * Creates a Calculator Context from a String
 * by parsing the language of the calculator.
 */
public class Parser {
    private String input;
    private int mode = 0;



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

    // TODO: fix the method to our needs
    public Context parse(String input) throws ParseException {
        this.input = input;
        Context context = new Context();

        if (mismatchedBrackets()) {
            throw new MismatchedBracketsException();
        }

        String[] parts = input.split("\\^");
        if (parts.length > 2) {
            throw new ParseException("Too many stack segments!");
        }

        List<IToken> tokens;

        if (parts.length == 2) {
            input = parts[0];
            tokens = parseTokens(input);
            context.getDataStack().addAll(tokens);

            input = parts[1];
        } else {
            input = parts[0];
        }

        tokens = parseTokens(input);
        Collections.reverse(tokens);
        context.getCodeStack().addAll(tokens);

        return context;
    }
}
