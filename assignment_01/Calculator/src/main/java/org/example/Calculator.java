package org.example;
import java.util.EmptyStackException;
import java.util.Stack;

public class Calculator {
    private Stack<Object> stack = new Stack<>();

    public static final double EPSILON = 0.00001; //TODO find appropiate range

    public Calculator(Stack<Object> stack) {
        this.stack = stack;
    }

    public Calculator() {

    }

    public void push(Object o) {
        if(o instanceof Integer || o instanceof Double || o instanceof String) {
            stack.push(o);
        } else {
            throw new IllegalArgumentException("Object to push does not match expected type");
        }
    }

    public Object pop() {
        if(stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }

    public void reset() {
        stack.clear();
    }

    public void executeCommand(char command) {
        switch (command) {
            case '+':
                add();
                break;
            case '-':
                subtract();
                break;
            case '*':
                multiply();
                break;
            case '/':
            case '%':
            case '&':
            case '|':
            case '_':
                nullCheck();
                break;
            case '~':
                negation();
                break;
            case '?':
                integerConversion();
                break;
            case '!':
            case '$':
            case '@':
            case '\\':
            case '#':
                stackSize();
                break;
            case '\'':
            case '"':
            case '=':
            case '<':
            case '>':
            default:
                throw new UnsupportedOperationException("Unsupported command: " + command);
        }
    }


    private void add() {
        Object a = stack.pop();
        Object b = stack.pop();

        if(a instanceof String || b instanceof String) {
            stack.push(String.valueOf(a) + b);
        }
        else if(a instanceof Double || b instanceof Double) {
            stack.push(toDouble(a) + toDouble(b));
        }
        else stack.push((Integer)a + (Integer) b);
    }

    private void subtract() {
        Object a = stack.pop();
        Object b = stack.pop();

        if(a instanceof String && b instanceof Integer) {
            if((Integer) b < 0 ) {
                throw new IllegalArgumentException("Subtraction requires a positive number");
            }
            stack.push(((String) a).substring((Integer) b ));

            return;
        }
        if(a instanceof Integer && b instanceof String) {
            if((Integer) a < 0 ) {
                throw new IllegalArgumentException("Subtraction requires a positive number");
            }
            stack.push(((String) b).substring(0, ((String) b).length()-((Integer) a)));
            return;
        }

        if(a instanceof Double || b instanceof Double) {
            if(a instanceof String || b instanceof String) throw new IllegalArgumentException("Cannot subtract String and Double");

            stack.push(toDouble(a) - toDouble(b));
        }
        else stack.push((Integer)a - (Integer)b);
    }

    private void multiply() {
        Object a = stack.pop();
        Object b = stack.pop();
        if(a instanceof String && b instanceof String) {
            throw new IllegalArgumentException("Cannot multiply two Strings");
        }
        if(a instanceof String && b instanceof Integer) {
            if((Integer) b < 0  || (Integer) b >= 128 ) {
                throw new IllegalArgumentException("Multiplication with Integer requires a number between 0 and 128");
            }
            stack.push(((String) a) + (char) ((Integer) b).intValue());
            return;
        }
        if(b instanceof String && a instanceof Integer) {
            if((Integer) a < 0  || (Integer) a >= 128 ) {
                throw new IllegalArgumentException("Multiplication with Integer requires a number between 0 and 128");
            }
            stack.push(((char) ((Integer) a).intValue() + (String) b));
            return;
        }
        if(a instanceof Double || b instanceof Double) {
            if(a instanceof String || b instanceof String) {
                throw new IllegalArgumentException("Cannot multiply String with Double");

            }
            stack.push(toDouble(a) * toDouble(b));
            return;
        }
        stack.push((Integer)a * (Integer)b);
    }

    private void negation() {
        Object a = stack.pop();

        if(a instanceof String) {
            stack.push("()");
            return;
        }
        if(a instanceof Double) {
            stack.push(-(Double) a);
            return;
        }
        stack.push(-(Integer) a);
    }

    private void stackSize() {
        stack.push(stack.size());
    }

    private void nullCheck() {
        Object a = stack.pop();

        if(a instanceof String) {
            if(a.equals("()")) {
                stack.push(1);
            } else stack.push(0);
        }
        if(a instanceof Double) {
            if(Math.abs((Double) a) < EPSILON) {
                stack.push(1);
            } else stack.push(0);
        }
        if(a instanceof Integer) {
            if((Integer) a == 0) stack.push(1);
            else stack.push(0);
        }
    }

    private void integerConversion() {
        Object a = stack.pop();

        if(a instanceof Integer || a instanceof String) {
            stack.push("()");
        } else {

            stack.push(((Double) a).intValue());
        }

    }




    /**
     * Simple helper function that casts both numeric Objects from the stack to double
     * without needing to typecheck every time
     *
     */
    private double toDouble(Object o) {
        if (o instanceof Integer) return ((Integer) o).doubleValue();
        if (o instanceof Double) return (Double) o;
        throw new IllegalArgumentException("Received NaN in toDouble");
    }
}
