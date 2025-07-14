package org.streams;

import java.util.Stack;

/**
 * Interface for input/output stream that a program can read from and write to.
 */

public interface IStream {
    /**
     * Read entire line of input until Enter is pressed (preferred way)
     * Characters are returned with their ASCII encoding.
     * @return Object representing the read line
     */
    public Object readLine();

    /**
     * Writes an object from stack to the stream. (return StringBuilder for easier testing)
     * @param o
     */
    public StringBuilder write(Object o);
}