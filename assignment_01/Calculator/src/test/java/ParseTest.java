import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests adapted in calculator language
 */
public class ParseTest {
    private Calculator calculator;
    private Context ctxt;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.ctxt = new Context();
        ctxt.setTestMode(true); // also sets commands stream to only "@"
        this.calculator = new Calculator(ctxt);
        this.calculator.reset(); // clear data stack
        this.parser = new Parser(calculator);
    }

    @Test
    void testIntegerParsing() throws Exception {
        calculator.push("123");
        parser.parseAll();
        assertEquals(123, calculator.pop());
    }

    @Test
    void testFloatingPointParsing() throws Exception {
        calculator.push("12.5");
        parser.parseAll();
        assertEquals(12.5, calculator.pop());
    }

    @Test
    void testSimpleStringBlockParsing() throws Exception {
        calculator.push("(abc)");
        parser.parseAll();
        assertEquals("abc", calculator.pop());
    }

    @Test
    void testSimpleStringParsing() throws Exception {
        calculator.push("(a)(b)(c)");
        parser.parseAll();
        assertEquals("c", calculator.pop());
    }

    @Test
    void testArithmeticOperators () throws Exception {
        interpretAndExpect("2 3 +", 5);
//        calculator.push("2 3 +");
//        parser.parseAll();
//        assertEquals((Integer) 5, calculator.pop());
    }

//    @Test
//    void testMixedInputSimple() throws Exception {
//        calculator.push("2(hi)3");
//        parser.parseAll();
//        assertEquals("2 hi 3 ▹ ", calculator.getContext().toString());
//    }
//
//    @Test
//    void testMixedInput() throws Exception {
//        interpretAndCheckState("2(hi)4 5.3+", "2 hi 9.3 ▹ ");
////        calculator.push("2(hi)4 5.3+");
////        parser.parseAll();
////        // 4 + 5.3 = 9.3, next string "hi" should be on the stack and 2
////        assertEquals("2 hi 9.3 ▹ ", calculator.getContext().toString());
//    }

    boolean interpretAndExpect(String expr, Object expected) throws Exception {
        calculator.push(expr);
        parser.parseAll();
        return calculator.pop() == expected;
    }

    boolean interpretAndCheckState(String expr, Object expected) throws Exception {
        calculator.push(expr);
        parser.parseAll();
        return calculator.getContext().toString() == expected;
    }
}