package org.streams;

import org.calculator.Context;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

public class InStream implements IStream {
    private final BufferedReader reader;

    public InStream() {
        this(new InputStreamReader(System.in));
    }

    public InStream(String input) {
        this(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
    }

    public InStream(InputStreamReader reader) {
        this.reader = new BufferedReader(reader);
    }

    /**
     * char by char reading
     *
     * @return int
     */
    public int read() {
        try {
            return reader.read();
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Read entire line of input until Enter is pressed (preferred way)
     * There is a command for reading a whole line of input
     * (terminated by “enter”) and converting it to an integer, floating-
     * point number, or string, depending on the contents.
     * If input is a string containing the sequence of ASCII characters.
     * Non-ASCII characters shall be ignored.
     *
     * @return line (input read)
     */
    @Override
    public Object readLine() {
        String line = readLineAsString();
        if (line == null) return null;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e2) {
                return line; // return trimmed ASCII-clean string
            }
        }
    }

    public String readLineAsString() {
        try {
            String line = reader.readLine();
            if (line != null) {
                // Remove non-ASCII characters
                line = line.replaceAll("[^\\x00-\\x7F]", "");
                // trim any leading or trailing spaces
                line = line.trim();
            }
            return line;
        } catch (IOException e) {
            throw new RuntimeException("Error reading input", e);
        }
    }

    @Override
    public void write(Object o, Boolean testMode) {
        throw new UnsupportedOperationException("Cannot write to input stream");
    }

    @Override
    public String getTestOuput(Boolean testMode) {
        throw new UnsupportedOperationException("Cannot write to input stream");
    }

    /**
     * Method to test input stream with keyboard
     * Tested with
     * café ☕ --> You entered: caf
     * @param args
     */
    public static void main(String[] args){
        Context ctxt = new Context();
        ctxt.setInputStream(new InStream());
        System.out.println("Please type something and press Enter:");
        Object input = ctxt.readInput();
        System.out.println("You entered: " + input);
    }

}
