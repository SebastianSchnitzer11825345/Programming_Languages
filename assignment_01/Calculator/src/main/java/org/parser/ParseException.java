package org.parser;

import java.io.Serial;

public class ParseException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;


    public ParseException() {
        super();
    }

    public ParseException(String msg) {
        super(msg);
    }
}