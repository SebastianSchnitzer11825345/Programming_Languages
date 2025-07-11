package org.tokens;

import org.calculator.Context;

/**
 * A token in the calculator.
 * Might be an integer, an operator, or a block
 */
public interface IToken {
    public void apply(Context ctx);
}

