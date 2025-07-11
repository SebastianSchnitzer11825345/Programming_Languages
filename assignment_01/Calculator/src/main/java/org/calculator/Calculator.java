package org.calculator;
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Stack;

public class Calculator {
    private Stack<Object> stack = new Stack<>();

    public static final double EPSILON = 0.00001; //TODO find appropiate range

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
                divide();
                break;
            case '%':
                modulo();
                break;
            case '&':
                land();
                break;
            case '|':
                lor();
                break;
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
                copy();
                break;
            case '$':
                delete();
                break;
            case '@':
            case '\\':
            case '#':
                stackSize();
                break;
            case '\'':
            case '"':
            case '=':
                comparison('=');
                break;
            case '<':
                comparison('<');
                break;
            case '>':
                comparison('>');
                break;
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

    private void divide() {
        Object a = stack.pop();
        Object b = stack.pop();

        if(a instanceof String && b instanceof String) {
            String stra = (String)a;
            String strb = (String)b;

            stack.push(stra.indexOf(strb));
        }
        else {
            Double da = toDouble(a);
            Double db = toDouble(b);
            if(db == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            stack.push(da / db);
        }
    }

    private void modulo() {
        Object a = stack.pop();
        Object b = stack.pop();

        if(a instanceof Float || b instanceof Float) {
            stack.push("()");
        } else if(a instanceof Integer && b instanceof Integer) {
            if((Integer) b == 0) {
                stack.push("()");
                return;
            }
            stack.push((Integer)a % (Integer)b);
        } else if (a instanceof String && b instanceof Integer) {
                if((Integer) b == 0 || (Integer) b >= ((String) a).length() ) {
                    stack.push("()");
                    return;
                }
                stack.push(Integer.valueOf((int)((String) a).charAt(((Integer) b))));


        } else {
            stack.push("()");
        }


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

    private void comparison(char command) {
        Object a = stack.pop();
        Object b = stack.pop();

        // Case: Both are Strings
        if(a instanceof String && b instanceof String) {
            compareStrings((String) a, (String) b, command);
            return;
        }
        // Case: Only a is a string
        if(a instanceof String) {
            switch(command) {
                case '=', '<':
                    stack.push(0);
                    break;
                case '>':
                    stack.push(1);
                    break;
                default: throw new IllegalArgumentException("Invalid comparison command");
            }
            return;
        }
        // Case: Only b is a string
        if(b instanceof String) {
                switch(command) {
                    case '=', '>':
                        stack.push(0);
                        break;
                    case '<':
                        stack.push(1);
                        break;
                    default: throw new IllegalArgumentException("Invalid comparison command");
                }
                return;
        }
        // Case a or b are a Float and the other an integer
        if(a instanceof Double || b instanceof Double) {
            Double da = toDouble(a);
            Double db = toDouble(b);
            boolean withinEpsilon = Math.abs(da) < 1.0 && Math.abs(db) < 1.0;
            switch(command) {
                case '=':
                    if(withinEpsilon) {
                        stack.push(Math.abs(da - db)<= EPSILON ? 1 : 0);
                    }
                    else {
                        stack.push(Math.abs(da - db) <= Math.max(da, db) * EPSILON ? 1 : 0);
                    }
                    break;
                case '<':
                    if(withinEpsilon) {
                        stack.push((db-da<= EPSILON && db-da > 0)  ? 1 : 0);
                    }
                    else {
                        stack.push((db-da<= EPSILON * Math.max(da,db) && db-da > 0) ? 1 : 0);
                    }
                    break;
                    case '>':
                        if(withinEpsilon) {
                            stack.push((da-db<= EPSILON && db-da > 0)  ? 1 : 0);
                        }
                        else {
                            stack.push((da-db<= EPSILON * Math.max(da,db) && db-da > 0) ? 1 : 0);
                        }
                        break;

                default: throw new IllegalArgumentException("Invalid comparison command");
            }

        } else {
            Integer ia = (Integer) a;
            Integer ib = (Integer) b;
                switch(command) {
                    case '=':
                        stack.push(Objects.equals(ia, ib) ? 1 : 0);
                        break;
                    case '<':
                        stack.push(ia < ib ? 1 : 0);
                        break;
                    case '>':
                        stack.push(ia > ib ? 1 : 0);
                        break;
                    default: throw new IllegalArgumentException("Invalid comparison command");
                }

        }


    }



    private void compareStrings(String a, String b, char command) {
        int result = a.compareTo(b);
        switch (command) {
            case '=':
                stack.push(result == 0 ? 1 : 0);
                break;
            case '<':
                stack.push(result < 0 ? 1 : 0);
                break;
            case '>':
                stack.push(result > 0 ? 1 : 0);
                break;
            default: throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    private void copy() {
        Object a = stack.pop();

        if(!(a instanceof Integer)) {
            stack.push(a);
            return;
        }
        if((Integer) a >= stack.size()) {
            stack.push(a);
            return;
        }

        stack.push(stack.elementAt((Integer) a));

    }
    //TODO Second opinion: Should logic operators on an integer strictly work on 0 and 1 or 0 and any other value?
    private void land() {
        Object a = stack.pop();
        Object b = stack.pop();

        if(a instanceof Integer && b instanceof Integer) {
            Integer ia = (Integer) a;
            Integer ib = (Integer) b;

            stack.push(ia != 0 && ib != 0 ? 1 : 0);
        }
        else {
            stack.push("()");
        }
    }


    private void lor() {
        Object a = stack.pop();
        Object b = stack.pop();

        if(a instanceof Integer && b instanceof Integer) {
            Integer ia = (Integer) a;
            Integer ib = (Integer) b;

            stack.push(ia != 0 || ib != 0 ? 1 : 0);
        }
        else {
            stack.push("()");
        }
    }

    private void delete() {
        Object a = stack.pop();

        if(!(a instanceof Integer)) {
            stack.push(a);
            return;
        }

        if((Integer) a >= stack.size()) {
            stack.push(a);
            return;
        }
        stack.removeElementAt((Integer) a);

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

    public int size() {
        return stack.size();
    }
}
