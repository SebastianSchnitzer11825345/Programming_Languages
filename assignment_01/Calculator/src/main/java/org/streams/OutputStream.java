package org.streams;

// TODO: implement formatting (like Display)

/**
 * Wrapper around System.out, writes integers as characters.
 */
public class OutputStream implements IStream {

    @Override
    public int read() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int i) {
        System.out.print(Character.toString((char) i));
    }
}
