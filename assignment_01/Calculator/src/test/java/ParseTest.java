import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class ParseTest {
    private Calculator calculator;
    private final Context ctxt = new Context();
    private Parser parser;

    @BeforeEach
    public void setUp() {
        ctxt.setTestMode(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
        calculator.getContext().clearCommandStream();
        this.parser = new Parser(calculator);
    }

    @Test
    void testIntegerParsing() throws Exception {
        calculator.getContext().addToCommandStreamInFront("123");
        parser.parseAll();
        assertEquals(123, calculator.pop());
    }

    @Test
    void testFloatingPointParsing() throws Exception {
        calculator.getContext().addToCommandStreamInFront("12.5");
        parser.parseAll();
        assertEquals(12.5, calculator.pop());
    }

    @Test
    void testSimpleStringBlockParsing() throws Exception {
        calculator.getContext().addToCommandStreamInFront("(abc)");
        parser.parseAll();
        assertEquals("abc", calculator.pop());
    }

    @Test
    void testSimpleStringParsing() throws Exception {
        calculator.getContext().addToCommandStreamInFront("(a)(b)(c)");
        parser.parseAll();
        assertEquals("c", calculator.pop());
    }

    @Test
    void testArithmeticOperators () throws Exception {
        calculator.push(2);
        calculator.push(3);
        calculator.getContext().addToCommandStreamInFront("+");
        parser.parseAll();
        assertEquals((Integer) 5, calculator.pop());
    }

    @Test
    void testMixedInputSimple() throws Exception {
        calculator.getContext().addToCommandStreamInFront("2(hi)3");
        parser.parseAll();
        assertEquals((Integer) 3, calculator.pop());
        assertEquals("hi", calculator.pop());
        assertEquals((Integer) 2, calculator.pop());
    }

    @Test
    void testMixedInput() throws Exception {
        calculator.getContext().addToCommandStreamInFront("2(hi)4 5.3+");
        parser.parseAll();
        assertEquals((Double) 9.3, calculator.pop()); // 4 + 5.3 = 9.3
        assertEquals("hi", calculator.pop()); // string should still be on the stack
        assertEquals((Integer) 2, calculator.pop()); // string should still be on the stack
    }
}

//    void testFloatingPointParsing() throws Exception {
//        calculator.getContext().addToCommandStreamInFront("12.5");
//        parser.parseAll();
//        Stack<Object> datastack = calculator.getContext().getDataStack();
//        System.out.println("Printing current data stack of size " + calculator.getContext().getStackSize());
//        for (Object object : datastack) {
//            System.out.println(object);
//        }
//
//        System.out.println("Looking up top stack element: " + calculator.getContext().getDataStack().peek());
//
//        assertEquals(12.5, calculator.pop());
//    }