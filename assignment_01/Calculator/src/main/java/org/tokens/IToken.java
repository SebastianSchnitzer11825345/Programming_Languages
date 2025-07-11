package org.tokens;

import org.example.Context;

/**
 * A token in the calculator.
 * Might be an integer, an operator, or a block
 */
public interface IToken {
//    public String getToken();

    public void apply(Context ctx);

    public void construct(Context ctx, int digit);

}

