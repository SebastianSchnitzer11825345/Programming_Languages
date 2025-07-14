package org.calculator;

import java.util.EmptyStackException;
import java.util.Stack;
import org.registers.RegisterSet;
import org.streams.IStream;
import org.streams.InStream;
import org.streams.OutputStream;



/**
 * Context captures data elements of calculator that exist in a state
 * * Command stream (A stream of characters regarded as commands to be
 *      executed in sequential order. When turned on initialized with
 *      contents of register a
 * * Data Stack (empty when calculator switched on)
 * * Register set (contain predefined values when switched on)
 * * Input stream
 * * Output stream
 */
public class Context {
    private RegisterSet registers ;
    private String commandStream;
    private Stack<Object> dataStack;
    private IStream inStream, outStream;


    /**
     * initialization when turning on
     */
    public Context() {
        this.registers = new RegisterSet();
        this.commandStream = ((String) registers.read('a'));
        this.dataStack = new Stack<>();
        this.inStream = new InStream();
        outStream = new OutputStream();
    }

    // Input stream
    public IStream getInputStream() {
        return inStream;
    }

    public void setInputStream(IStream inStream) {
        this.inStream = inStream;
    }

    public Object readInput() {
        return this.inStream.readLine();
    }

    //  Output stream
    public IStream getOutputStream() {
        return outStream;
    }

    public void setOutputStream(IStream outStream) {
        this.outStream = outStream;
    }

    public StringBuilder writeOutput(Object o) {
        return this.outStream.write(o);
    }

    // Command Stream
    public String getCommandStream() {
        return commandStream;
    }

    public void addToCommandStreamInFront(Object command) {
        this.commandStream = command + this.commandStream;
    }

    // Registers
    public RegisterSet getRegisters() {
        return registers;
    }

    // Data Stack
    public Stack<Object> getDataStack() {
        return dataStack;
    }

    public void push(Object o) {
        if(o instanceof Integer || o instanceof Double || o instanceof String) {
            dataStack.push(o);
        } else {
            throw new IllegalArgumentException("Object to push does not match expected type");
        }
    }

    public Object pop() {
        if(dataStack.isEmpty()) {
            throw new EmptyStackException();
        }
        return dataStack.pop();
    }

    public void removeElementAt (int index) {
        dataStack.removeElementAt(index);
    }

    public Object getElementAt (int index) {
        return dataStack.get(index);
    }

    public void reset() {
        dataStack.clear();
    }

    public Integer getStackSize() {
        return dataStack.size();
    }

    public void setStackSize(Integer stackSize) {}


    // not used at this point

    public Integer nextInt() {
        if (!dataStack.isEmpty()) {
            Object token = dataStack.peek();
            if (token instanceof Integer)
                return (Integer) dataStack.pop();
        }
        throw new CalculatorException("Expected Integer on data stack");
    }

    public Double nextDecimal() {
        if (!dataStack.isEmpty()) {
            Object token = dataStack.peek();
            if (token instanceof Double || token instanceof Integer)
                return (Double) dataStack.pop();
        }

        throw new CalculatorException("Expected Decimal point number on data stack");
    }

    public String nextString() {
        if (!dataStack.isEmpty()) {
            Object token = dataStack.peek();
            if (token instanceof String)
                return (String) dataStack.pop();
        }

        throw new CalculatorException("Expected String token on data stack");
    }

//    public String nextBlock() {
//        if (dataStack.size() > 0) {
//            Object token = dataStack.peek();
//            if (token == "(")
//                // TODO: if we need this then still need to implement
//                return (String) dataStack.pop();
//        }
//
//        throw new CalculatorException("Expected Block on data stack");
//    }

}
