package org.calculator;

import java.util.Stack;

import org.registers.RegisterSet;
import org.streams.IStream;
import org.streams.InStream;
import org.streams.OutputStream;
import org.tokens.*;

/**
 * Context captures all elements of calculator that exist in a state
 */
public class Context {
    private Stack<IToken> dataStack;
    private RegisterSet registers;
    private IStream inStream, outStream;
    public String commandStream;

    public Context() {
        this(new Stack<IToken>(), "");
    }

    public Context(Stack<IToken> dataStack, String commandStream) {
        this.dataStack = dataStack;
        this.commandStream = commandStream;

        registers = new RegisterSet();
        inStream = new InStream();
        outStream = new OutputStream();
    }

    public IStream getInputStream() {
        return inStream;
    }

    public IStream getOutputStream() {
        return outStream;
    }

    public void setInputStream(IStream inStream) {
        this.inStream = inStream;
    }

    public void setOutputStream(IStream outStream) {
        this.outStream = outStream;
    }

    public Stack<IToken> getDataStack() {
        return dataStack;
    }

    public RegisterSet getRegisters() {
        return registers;
    }

    public String getCommandStream() {
        return commandStream;
    }

    public IntegerToken nextInt() {
        if (dataStack.size() > 0) {
            IToken token = dataStack.peek();
            if (token instanceof IntegerToken)
                return (IntegerToken) dataStack.pop();
        }

        throw new CalculatorException("Expected Integer on data stack");
    }

    public FloatingPointToken nextFloat() {
        if (dataStack.size() > 0) {
            IToken token = dataStack.peek();
            if (token instanceof FloatingPointToken)
                return (FloatingPointToken) dataStack.pop();
        }

        throw new CalculatorException("Expected Floating point number on data stack");
    }

    public IntegerToken nextString() {
        if (dataStack.size() > 0) {
            IToken token = dataStack.peek();
            if (token instanceof StringToken)
                return (StringToken) dataStack.pop();
        }

        throw new CalculatorException("Expected String token on data stack");
    }

    public BlockElement nextBlock() {
        if (dataStack.size() > 0) {
            IToken token = dataStack.peek();
            if (token instanceof BlockElement)
                return (BlockElement) dataStack.pop();
        }

        throw new CalculatorException("Expected Block on data stack");
    }

}
