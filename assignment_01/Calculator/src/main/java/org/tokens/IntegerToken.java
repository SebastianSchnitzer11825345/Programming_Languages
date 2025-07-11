package org.tokens;

import org.calculator.Context;

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

    /**
     * Construct method used when digit already on stack and we add on top
     * @param ctx
     * @param digit
     */
    public void construct(Context ctx, int digit) {
        this.value = this.value * 10 + digit;
        ctx.getDataStack().push(this);
    }

}
