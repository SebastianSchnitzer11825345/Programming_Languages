package org.streams;

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
     * @return
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
     * @return line (input read)
     */
    @Override
    public Object readLine() {
//            System.out.print("> ");
//            System.out.print("Press Enter to terminate input stream:");
        String line = readLineAsString();
        if (line == null) return null;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e2) {
                return line;
            }
        }
    }

    public String readLineAsString() {
        try {
            String line = reader.readLine();
            return line != null ? line.trim() : null;
        } catch (IOException e) {
            throw new RuntimeException("Error reading input", e);
        }
    }

    @Override
    public String write(Object o, Boolean testMode) {
        throw new UnsupportedOperationException("Cannot write to input stream");
    }


    public static void main(String[] args) {
        InStream inStream = new InStream();
        System.out.print("Please type something: ");
        Object line = inStream.readLine();
        System.out.println("You typed: " + line);
    }
}
