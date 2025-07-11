package org.tokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.calculator.Context;

public class BlockElement implements IToken {

    private List<IToken> tokens = new ArrayList<>();

    public BlockElement() {
    }

    public BlockElement(Collection<IToken> tokens) {
        this.tokens.addAll(tokens);
    }

    @Override
    public void apply(Context ctxt) {
        ctxt.getDataStack().push(this);
    }

    public List<IToken> getTokens() {
        return this.tokens;
    }

    @Override
    public String toString() {
        String inner = tokens.stream().map(e->e.toString()).collect(Collectors.joining(" "));
        return "(" + inner + ")";
    }

}
