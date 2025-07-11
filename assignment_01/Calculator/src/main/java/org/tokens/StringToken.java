package org.tokens;

import org.example.Context;

public class StringToken implements IToken {
    String statement;

    public StringToken(String statement) {
        this.statement = statement;
    }

    public String getValue() {
        return statement;
    }

    @Override
    public void apply(Context ctx) {
        ctx.getDataStack().push(this);
    }
}
