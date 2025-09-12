package org.streams;

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
     * @param o, testMode
     */
    public void write(Object o, Boolean testMode);

    /**
     * Get Output for tests
     * @param testMode only when true
     * @return String of generated output
     */
    public String getTestOuput(Boolean testMode);
}