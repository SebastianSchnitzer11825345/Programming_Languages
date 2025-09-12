package TestsInJava;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Command stream logic
 */
public class CommandStreamTest {
    private Calculator calculator;
    private final Context ctxt = new Context();
    private Parser parser;

    @BeforeEach
    public void setUp() {
        ctxt.setTestModeOld(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
        calculator.getContext().clearCommandStream();
        this.parser = new Parser(calculator);
    }

    @Test
    void testIntegerParsing() {
        calculator.getContext().addToCommandStreamInFront("123");
        parser.parseAll();
        assertEquals(123, calculator.pop());
    }

    @Test
    void testFloatingPointParsing() {
        calculator.getContext().addToCommandStreamInFront("12.5");
        parser.parseAll();
        assertEquals(12.5, calculator.pop());
    }

    @Test
    void testSimpleStringBlockParsing() {
        calculator.getContext().addToCommandStreamInFront("(abc)");
        parser.parseAll();
        assertEquals("abc", calculator.pop());
    }

    @Test
    void testSimpleStringParsing() {
        calculator.getContext().addToCommandStreamInFront("(a)(b)(c)");
        parser.parseAll();
        assertEquals("c", calculator.pop());
    }

    @Test
    void testArithmeticOperators () {
        calculator.push(2);
        calculator.push(3);
        calculator.getContext().addToCommandStreamInFront("+");
        parser.parseAll();
        assertEquals( 5, calculator.pop());
    }

    @Test
    void testMixedInputSimple() {
        calculator.getContext().addToCommandStreamInFront("2(hi)3");
        parser.parseAll();
        assertEquals( 3, calculator.pop());
        assertEquals("hi", calculator.pop());
        assertEquals(2, calculator.pop());
    }

    @Test
    void testMixedInput() {
        calculator.getContext().addToCommandStreamInFront("2(hi)4 5.3+");
        parser.parseAll();
        assertEquals(9.3, calculator.pop()); // 4 + 5.3 = 9.3
        assertEquals("hi", calculator.pop()); // string should still be on the stack
        assertEquals(2, calculator.pop()); // string should still be on the stack
    }
}
