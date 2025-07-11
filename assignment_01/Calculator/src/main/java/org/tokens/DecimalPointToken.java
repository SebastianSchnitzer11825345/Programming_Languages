package org.tokens;

import org.example.Context;

public class DecimalPointToken implements IToken {
    double value;

    public DecimalPointToken(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void apply(Context ctx) {
        ctx.getDataStack().push(this);
    }

    public void construct(Context ctx, int digit, int m) {
        this.value = this.value + digit * Math.pow(10, m + 1);
        ctx.getDataStack().push(this);
    }

//    public void construct2(Context ctx, double value, char direction) {
//        switch (direction) {
//            case 'l':
//                this.value = this.value * 10 + digit;
//                break;
//            case 'r':
//                this.value = this.value * 10 - digit;
//                break;
//        }
//        ctx.getDataStack().push(this);
//    }

}
