package org.tokens;

import org.calculator.Context;

public class FloatingPointToken implements IToken {
    float value;

    public FloatingPointToken(int value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void apply(Context ctx) {
        ctx.getDataStack().push(this);
    }
}
