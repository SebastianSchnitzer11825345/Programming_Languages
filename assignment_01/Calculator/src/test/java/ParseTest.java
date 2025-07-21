import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;
import org.registers.RegisterSet;
import static org.junit.jupiter.api.Assertions.*;

public class   ParseTest {
    private Calculator calculator;
    private Context ctxt = new Context();

    @BeforeEach
    public void setUp() {
        ctxt.setTestMode(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
    }

    @Test
    void testIntegerParsing() throws Exception {
        Parser parser = new Parser("123", calculator);
        parser.parseAll();
        assertEquals(123, calculator.pop());
    }

    @Test
    void testFloatingPointParsing() throws Exception {
        Parser parser = new Parser("12.5", calculator);
        parser.parseAll();
        assertEquals(12.5, calculator.pop());
    }

    @Test
    void testSimpleStringBlockParsing() throws Exception {
        Parser parser = new Parser("(abc)", calculator);
        parser.parseAll();
        assertEquals("abc", calculator.pop());
    }

    @Test
    void testSimpleStringParsing() throws Exception {
        Parser parser = new Parser("(a)(b)(c)", calculator);
        parser.parseAll();
        assertEquals("c", calculator.pop());
    }

    @Test
    void testArithmeticOperators () throws Exception {
        calculator.push(2);
        calculator.push(3);
        Parser parser = new Parser("+", calculator);
        parser.parseAll();
        assertEquals((Integer) 5, calculator.pop());
    }

    @Test
    void testMixedInputSimple() throws Exception {
        Parser parser = new Parser("2(hi)3", calculator);
        parser.parseAll();
        assertEquals((Integer) 3, calculator.pop());
        assertEquals("hi", calculator.pop());
        assertEquals((Integer) 2, calculator.pop());
    }

    @Test
    void testMixedInput() throws Exception {
        Parser parser = new Parser("2(hi)4 5.3+", calculator);
        parser.parseAll();
        assertEquals((Double) 9.3, calculator.pop()); // 4 + 5.3 = 9.3
        assertEquals("hi", calculator.pop()); // string should still be on the stack
        assertEquals((Integer) 2, calculator.pop()); // string should still be on the stack
    }
}