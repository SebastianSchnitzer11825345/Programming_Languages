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
 * * Register set (contain predefined values when switched on)
 * * Input stream
 * * Output stream
 */
public class Context {
    private final RegisterSet registers = new RegisterSet() ;
    private StringBuilder commandStream = new StringBuilder();
    private Stack<Object> dataStack;
    private IStream inStream, outStream;
    private boolean testMode = false;

    /**
     * initialization when turning on
     */
    public Context() {
        this.dataStack = new Stack<>();
        this.inStream = new InStream();
        outStream = new OutputStream();
    }

    /**
     * Sets the calculator into test mode and replaces register a with only "@"
     * @param mode
     */
    public void setTestModeOld(boolean mode) {
        this.testMode = mode;
        clearCommandStream();
        String startCommand = "@";
        addToCommandStreamInFront(startCommand);
    }

    public void setTestMode(boolean mode) {
        this.testMode = mode;
        clearCommandStream();
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

    public void removeExecCharFromCommandStream() {
        if (!commandStream.isEmpty()) {
            commandStream.deleteCharAt(0);
        }
    }

    /**
     * Adds input to Command stream at the front, should be string format
     * @param command as String
     */
    public void addToCommandStreamInFront(String command) {
        commandStream.insert(0, command);
    }

    /**
     * Adds input to Command stream at the end, should be string format
     * @param command as String
     */
    public void addToCommandStreamInBack(String command) {
        commandStream.append(command);
    }

    public void clearCommandStream() {
        if (testMode)
            commandStream = new StringBuilder("");
    }

    // Registers (since read-only using interface to protect)
    public IReadOnlyRegisters getRegisters() {
        return registers;
    }

    public void loadRegister(char c) {
        commandStream.append(registers.read(c));
    }


    // Data Stack
    public Stack<Object> getDataStack() {
        return dataStack;
    }

    public void push(Object o) {
        if(o instanceof Integer || o instanceof Double || o instanceof String) {
            dataStack.push(o);
        } else {
            throw new IllegalArgumentException("Object to push does not match expected type" + o.getClass());
        }
    }

    public Object pop() throws EmptyStackException {
        if (dataStack.peek() != null) {
            return dataStack.pop();
        } else throw new EmptyStackException();
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
        List<String> dataStackState = new ArrayList<>();
        for (Object e : dataStack) {
            dataStackState.add(e.toString());
        }
        StringBuilder state = new StringBuilder();
        state.append(String.join(" ,", dataStackState));

        state.append(' ' + String.valueOf('\u25B9') + ' ');
        state.append(commandStream.toString());
        return state.toString();

    }
}
