package org.streams;

// TODO: implement formatting (like Display)

import org.calculator.Calculator;

/**
 * A stream of characters displayed on the screen
 * * If the value is a string, the characters in the string are directly
 *   written to the output stream (without additional parentheses).
 * * If the value is a number (integer or floating-point number), it is
 *   written to the output stream in an appropriate format
 *   (beginning with - for negative numbers and avoiding unnecessary digits)
 */
public class OutputStream implements IStream {
    private final StringBuilder buffer = new StringBuilder();

    public OutputStream() {}

    @Override
    public Object readLine() {
        throw new UnsupportedOperationException();
    }

    //TODO: and the numbers of words, letters, digits, white-space characters
    // and special characters in the string (for each category counted separately).

    /**
     * Print out data stack object (run mode) or
     * store data stack object in buffer (for Test mode)
     * @param o Object to write to the stream.
     */
    @Override
    public String write(Object o, Boolean testMode ) {
        if (testMode) {
            return writeOutputToBuffer(o);  // test mode: return StringBuilder
        } else {
            writeOutputToScreen(o);         // run mode: print to screen
            return null;
        }

    }

    public String writeOutputToBuffer(Object o) {
        buffer.setLength(0);

        if (o instanceof Integer || o instanceof String) {
            buffer.append(o);
        } else if (o instanceof Double) {
            double d = (Double) o;
            if (d == (long) d) {
                buffer.append((long) d);
            } else {
                buffer.append(d);
            }
        }
        return buffer.toString();
    }

    public void writeOutputToScreen(Object element) {
        System.out.println(writeOutputToBuffer(element));  // reuse logic
    }

}
