package org.streams;

public class CommandStream implements IStream {

    @Override
    public int read() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int i) {
        System.out.print(Character.toString((char) i));
    }

}
