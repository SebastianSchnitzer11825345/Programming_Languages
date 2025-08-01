package org.calculator;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

import org.registers.IReadOnlyRegisters;
import org.registers.RegisterSet;
import org.streams.IStream;
import org.streams.InStream;
import org.streams.OutputStream;

/**
 * Context captures data elements of calculator that exist in a state
 * * Command stream (A stream of characters regarded as commands to be
 *      executed in sequential order. When turned on initialized with
 *      contents of register a)
 * * Data Stack (empty when calculator switched on)
 *   - allowed are up to 10 elements on data stack (TODO: check if 10 is enough)
 * * Register set (contain predefined values when switched on)
 * * Input stream
 * * Output stream
 */
public class Context {
    private final RegisterSet registers = new RegisterSet() ;
    private StringBuilder commandStream = new StringBuilder();
    private Stack<Object> dataStack;
    private IStream inStream, outStream;
    // TODO: only used for current testing, remove later if not needed
    private boolean testMode = false;

    /**
     * initialization when turning on
     */
    public Context() {
        commandStream.append(registers.read('a'));
        this.dataStack = new Stack<>();
        this.inStream = new InStream();
        outStream = new OutputStream();
    }

    public void setTestMode(boolean mode) {
        this.testMode = mode;
    }

    public boolean isTestMode() {
        return testMode;
    }

    // Input stream
    public IStream getInputStream() {
        return inStream;
    }

    public void setInputStream(IStream inStream) {
        this.inStream = inStream;
    }

    public Object readInput() {
        return inStream.readLine();
    }

    //  Output stream
    public IStream getOutputStream() {
        return outStream;
    }

    public void setOutputStream(IStream outStream) {
        this.outStream = outStream;
    }

    public void writeOutput(Object o) {
        outStream.write(o, testMode);
    }

    public String getOutputForTest() {
        return outStream.getTestOuput(testMode);
    }

    // Command Stream
    public StringBuilder getCommandStream() {
        return commandStream;
    }

//    public void removeExecCharFromCommandStream() {
//        if (!commandStream.isEmpty()) {
//            commandStream = commandStream.substring(1);
//        }
//    }

    public void removeExecCharFromCommandStream() {
        if (commandStream.length() > 0) {
            commandStream.deleteCharAt(0);
        }
    }

    // TODO: check if Object or String, StringBuilder
    public void addToCommandStreamInFront(String command) {
        commandStream.insert(0, command);
    }

    public void clearCommandStream() {
        if (testMode)
            commandStream = new StringBuilder("");
    }

    // Registers (since read-only using interface to protect)
    public IReadOnlyRegisters getRegisters() {
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

    public Object peek() {
        if(dataStack.isEmpty()) {
            throw new EmptyStackException();
        }
        return dataStack.peek();
    }

    public void removeElementAt(int index) {
        dataStack.removeElementAt(index);
    }

    public Object getElementAt(int index) {
        return dataStack.get(index);
    }

    public void reset() {
        dataStack.clear();
    }

    public Integer getStackSize() {
        return dataStack.size();
    }

    @Override
    public String toString() {
        List<String> result = new ArrayList<>();
        for (Object e : dataStack) {
            result.add(e.toString());
        }
        result.add(String.valueOf('\u25B9'));
        int insertionPoint = result.size();
        result.add(insertionPoint, commandStream.toString());

        return String.join(" ", result);
    }
    // TODO: delete this whole section later if not used
    public void setStackSize(int stackSize) {
        dataStack.setSize(stackSize);
    }


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
}
