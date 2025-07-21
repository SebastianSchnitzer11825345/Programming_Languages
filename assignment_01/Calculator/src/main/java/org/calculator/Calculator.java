package org.calculator;
import org.parser.ParseException;
import org.parser.Parser;

import java.util.EmptyStackException;
import java.util.Objects;

//TODO: In addition to above cases, an error is reported if the data stack
// does not have enough entries. If an error occurs, the calculator
// simply stops its execution and gives an error message.

public class Calculator {
//    private Stack<Object> stack = new Stack<>();
    private Context ctxt;
    public static final double EPSILON = 0.00001;

    public Calculator(Context ctx) {
        this.ctxt = ctx;
    }

    public Context getContext() {
        return ctxt;
    }

    public void push(Object o) {
        if(o instanceof Integer || o instanceof Double || o instanceof String) {
            ctxt.push(o);
        } else {
            throw new IllegalArgumentException("Object to push does not match expected type");
        }
    }

    public Object pop() {
        if(ctxt.getDataStack().isEmpty()) {
            throw new EmptyStackException();
        }
        return ctxt.pop();
    }

    public void reset() {
        ctxt.reset();
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
                applyImmediately();
                break;
            case '\\':
            case '#':
                stackSize();
                break;
            case '\'':
                readInput();
                break;
            case '"':
                writeOutput();
                break;
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
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        if(a instanceof String || b instanceof String) {
            ctxt.push(String.valueOf(a) + b);
        }
        else if(a instanceof Double || b instanceof Double) {
            ctxt.push(toDouble(a) + toDouble(b));
        }
        else ctxt.push((Integer)a + (Integer) b);
    }

    private void subtract() {
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        if(a instanceof String && b instanceof Integer) {
            if((Integer) b < 0 ) {
                throw new IllegalArgumentException("Subtraction requires a positive number");
            }
            ctxt.push(((String) a).substring((Integer) b ));

            return;
        }
        if(a instanceof Integer && b instanceof String) {
            if((Integer) a < 0 ) {
                throw new IllegalArgumentException("Subtraction requires a positive number");
            }
            ctxt.push(((String) b).substring(0, ((String) b).length()-((Integer) a)));
            return;
        }

        if(a instanceof Double || b instanceof Double) {
            if(a instanceof String || b instanceof String) throw new IllegalArgumentException("Cannot subtract String and Double");

            ctxt.push(toDouble(a) - toDouble(b));
        }
        else ctxt.push((Integer)a - (Integer)b);
    }

    private void multiply() {
        Object a = ctxt.pop();
        Object b = ctxt.pop();
        if(a instanceof String && b instanceof String) {
            throw new IllegalArgumentException("Cannot multiply two Strings");
        }
        if(a instanceof String && b instanceof Integer) {
            if((Integer) b < 0  || (Integer) b >= 128 ) {
                throw new IllegalArgumentException("Multiplication with Integer requires a number between 0 and 128");
            }
            ctxt.push(((String) a) + (char) ((Integer) b).intValue());
            return;
        }
        if(b instanceof String && a instanceof Integer) {
            if((Integer) a < 0  || (Integer) a >= 128 ) {
                throw new IllegalArgumentException("Multiplication with Integer requires a number between 0 and 128");
            }
            ctxt.push(((char) ((Integer) a).intValue() + (String) b));
            return;
        }
        if(a instanceof Double || b instanceof Double) {
            if(a instanceof String || b instanceof String) {
                throw new IllegalArgumentException("Cannot multiply String with Double");

            }
            ctxt.push(toDouble(a) * toDouble(b));
            return;
        }
        ctxt.push((Integer)a * (Integer)b);
    }

    private void divide() {
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        if(a instanceof String && b instanceof String) {
            String stra = (String)a;
            String strb = (String)b;

            ctxt.push(stra.indexOf(strb));
        }
        else {
            Double da = toDouble(a);
            Double db = toDouble(b);
            if(db == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            ctxt.push(da / db);
        }
    }

    private void modulo() {
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        if(a instanceof Float || b instanceof Float) {
            ctxt.push("()");
        } else if(a instanceof Integer && b instanceof Integer) {
            if((Integer) b == 0) {
                ctxt.push("()");
                return;
            }
            ctxt.push((Integer)a % (Integer)b);
        } else if (a instanceof String && b instanceof Integer) {
                if((Integer) b == 0 || (Integer) b >= ((String) a).length() ) {
                    ctxt.push("()");
                    return;
                }
                ctxt.push(Integer.valueOf((int)((String) a).charAt(((Integer) b))));


        } else {
            ctxt.push("()");
        }


    }

    private void negation() {
        Object a = ctxt.pop();

        if(a instanceof String) {
            ctxt.push("()");
            return;
        }
        if(a instanceof Double) {
            ctxt.push(-(Double) a);
            return;
        }
        ctxt.push(-(Integer) a);
    }

    private void stackSize() {
        ctxt.push(ctxt.getStackSize());
    }

    private void nullCheck() {
        Object a = ctxt.pop();

        if(a instanceof String) {
            if(a.equals("()")) {
                ctxt.push(1);
            } else ctxt.push(0);
        }
        if(a instanceof Double) {
            if(Math.abs((Double) a) < EPSILON) {
                ctxt.push(1);
            } else ctxt.push(0);
        }
        if(a instanceof Integer) {
            if((Integer) a == 0) ctxt.push(1);
            else ctxt.push(0);
        }
    }

    private void integerConversion() {
        Object a = ctxt.pop();

        if(a instanceof Integer || a instanceof String) {
            ctxt.push("()");
        } else {

            ctxt.push(((Double) a).intValue());
        }

    }

    private void comparison(char command) {
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        // Case: Both are Strings
        if(a instanceof String && b instanceof String) {
            compareStrings((String) a, (String) b, command);
            return;
        }
        // Case: Only a is a string
        if(a instanceof String) {
            switch(command) {
                case '=', '<':
                    ctxt.push(0);
                    break;
                case '>':
                    ctxt.push(1);
                    break;
                default: throw new IllegalArgumentException("Invalid comparison command");
            }
            return;
        }
        // Case: Only b is a string
        if(b instanceof String) {
                switch(command) {
                    case '=', '>':
                        ctxt.push(0);
                        break;
                    case '<':
                        ctxt.push(1);
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
                        ctxt.push(Math.abs(da - db)<= EPSILON ? 1 : 0);
                    }
                    else {
                        ctxt.push(Math.abs(da - db) <= Math.max(da, db) * EPSILON ? 1 : 0);
                    }
                    break;
                case '<':
                    if(withinEpsilon) {
                        ctxt.push((db-da<= EPSILON && db-da > 0)  ? 1 : 0);
                    }
                    else {
                        ctxt.push((db-da<= EPSILON * Math.max(da,db) && db-da > 0) ? 1 : 0);
                    }
                    break;
                    case '>':
                        if(withinEpsilon) {
                            ctxt.push((da-db<= EPSILON && db-da > 0)  ? 1 : 0);
                        }
                        else {
                            ctxt.push((da-db<= EPSILON * Math.max(da,db) && db-da > 0) ? 1 : 0);
                        }
                        break;

                default: throw new IllegalArgumentException("Invalid comparison command");
            }

        } else {
            Integer ia = (Integer) a;
            Integer ib = (Integer) b;
                switch(command) {
                    case '=':
                        ctxt.push(Objects.equals(ia, ib) ? 1 : 0);
                        break;
                    case '<':
                        ctxt.push(ia < ib ? 1 : 0);
                        break;
                    case '>':
                        ctxt.push(ia > ib ? 1 : 0);
                        break;
                    default: throw new IllegalArgumentException("Invalid comparison command");
                }

        }


    }



    private void compareStrings(String a, String b, char command) {
        int result = a.compareTo(b);
        switch (command) {
            case '=':
                ctxt.push(result == 0 ? 1 : 0);
                break;
            case '<':
                ctxt.push(result < 0 ? 1 : 0);
                break;
            case '>':
                ctxt.push(result > 0 ? 1 : 0);
                break;
            default: throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    private void copy() {
        Object a = ctxt.peek();

        if(!(a instanceof Integer) || (a).equals(ctxt.getStackSize())) {
            return;
        }


        if((Integer) a < 0 || (Integer) a > ctxt.getStackSize()) {
            return;
        }

        a = ctxt.pop();

        ctxt.push(ctxt.getElementAt(ctxt.getStackSize() - (Integer) a));

    }

    private void land() {
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        if(a instanceof Integer && b instanceof Integer) {
            Integer ia = (Integer) a > 0 ? 1 : 0; // changed the logic as defined above
            Integer ib = (Integer) b > 0 ? 1 : 0; // changed the logic as defined above

            ctxt.push(ia != 0 && ib != 0 ? 1 : 0);
        }
        else {
            ctxt.push("()");
        }
    }


    private void lor() {
        Object a = ctxt.pop();
        Object b = ctxt.pop();

        if(a instanceof Integer && b instanceof Integer) {
            Integer ia = (Integer) a > 0 ? 1 : 0; // changed the logic as defined above
            Integer ib = (Integer) b > 0 ? 1 : 0; // changed the logic as defined above

            ctxt.push(ia != 0 || ib != 0 ? 1 : 0);
        }
        else {
            ctxt.push("()");
        }
    }

    private void delete() {
        Object a = ctxt.peek();

        //"Pops only from the data stack if the top entry is not an integer in the appropriate range."
        if(!(a instanceof Integer)) {
            return;
        }

        if((Integer) a >= ctxt.getStackSize()-1) {
            return;
        }
         a = ctxt.pop();

        ctxt.removeElementAt(ctxt.getStackSize() - (Integer) a);



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
        return ctxt.getStackSize();
    }

    public void applyImmediately() {
        ctxt.addToCommandStreamInFront(ctxt.pop());
    }

    public void readInput() {
        Object input = ctxt.readInput();
        ctxt.addToCommandStreamInFront(input);
    }

    public String writeOutput() {
        return ctxt.writeOutput(ctxt.pop());
    }
}
