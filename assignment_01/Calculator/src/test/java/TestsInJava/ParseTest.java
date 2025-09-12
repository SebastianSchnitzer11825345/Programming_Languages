package TestsInJava;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;

/**
 * Tests adapted in calculator language
 * Tests have @ in them so anything pushed to stack is executed
 */
public class ParseTest {
    private Calculator calculator;
    private Context ctxt;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.ctxt = new Context();
        ctxt.setTestModeOld(true); // also sets commands stream to only "@"
        this.calculator = new Calculator(ctxt);
        this.calculator.reset(); // clear data stack
        this.parser = new Parser(calculator);
    }

    @Test
    void testIntegerParsing() {
        interpretAndExpect("123", 123);
    }

    @Test
    void testFloatingPointParsing() {
        interpretAndExpect("12.5", 12.5);
    }

    @Test
    void testDoubleFloatingPointParsing() {
        interpretAndExpect("12.5.37", 0.37);
    }

    @Test
    void testDoubleFloatingPointParsing2() {
        interpretAndExpect("12.5 0.37", 0.37);
    }

    @Test
    void testSimpleStringParsing() {
        interpretAndExpect("(abc)", "abc");
    }

    @Test
    void testSimpleStringMultiElementsParsing() {
        interpretAndExpect("(a)(b)(c)", 'c');
    }

    @Test
    void testArithmeticOperators () {
        interpretAndExpect("2 3 +", 5);
    }

    @Test
    void testMixedInputSimple() {
        interpretAndCheckState("2(hi)3", "2 hi 3 ▹ ");
    }

    @Test
    void testMixedInput() {
        interpretAndCheckState("2(hi)4 5.3+", "2 hi 9.3 ▹ ");
    }

    boolean interpretAndExpect(String expr, Object expected) {
        calculator.push(expr);
        parser.parseAll();
        return calculator.pop() == expected;
    }

    boolean interpretAndCheckState(String expr, Object expected) {
        calculator.push(expr);
        parser.parseAll();
        return calculator.getContext().toString() == expected;
    }
}