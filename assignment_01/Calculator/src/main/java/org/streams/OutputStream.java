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
     * Store data stack objects in buffer
     * @param o Object to write to the stream.
     */
    @Override
    public StringBuilder write(Object o) {
        buffer.setLength(0);

        if (o instanceof Integer) {
            buffer.append(o);
        }
        else if (o instanceof Double) {
            double d = (Double) o;
            if (d == (long) d) {
                // It's a whole number, print without decimal
                buffer.append((long) d);
            } else {
                // Keep meaningful decimal digits only
                buffer.append(d);
            }
        }
        else {
            buffer.append(o.toString());
        }
        return buffer;
    }

}
