package org.parser;

import java.io.Serial;

public class MismatchedBracketsException extends ParseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MismatchedBracketsException() {
        super("Mismatched Brackets!");
    }
}