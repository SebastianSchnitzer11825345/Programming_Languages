package org.tokens;

import org.example.Context;

public class IntegerToken implements IToken {
    int value;

    public IntegerToken(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void apply(Context ctx) {
        ctx.getDataStack().push(this);
    }

}
