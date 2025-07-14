import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.Test;
import org.parser.Parser;
import org.registers.RegisterSet;
import static org.junit.jupiter.api.Assertions.*;

public class ParseTest {
    @Test
    void testIntegerParsing() throws Exception {
        Context ctxt = new Context();
        Calculator calculator = new Calculator();
        RegisterSet registers = new RegisterSet();
        Parser parser = new Parser("123", calculator, ctxt, registers);
        parser.parseAll();
        assertEquals(123, ctxt.pop());
    }

    @Test
    void testFloatingPointParsing() throws Exception {
        Context ctxt = new Context();
        Calculator calculator = new Calculator();
        RegisterSet registers = new RegisterSet();
        Parser parser = new Parser("12.5", calculator, ctxt, registers);
        parser.parseAll();
        assertEquals(12.5, ctxt.pop());
    }

    @Test
    void testSimpleStringParsing() throws Exception {
        Context ctxt = new Context();
        Calculator calculator = new Calculator();
        RegisterSet registers = new RegisterSet();
        Parser parser = new Parser("(abc)", calculator, ctxt, registers);
        parser.parseAll();
        assertEquals("abc", ctxt.pop());
    }

    @Test
    void testArithmeticOperators () throws Exception {
        Context ctxt = new Context();
        Calculator calculator = new Calculator();
        RegisterSet registers = new RegisterSet();
        ctxt.push(2);
        ctxt.push(3);
        Parser parser = new Parser("+", calculator, ctxt, registers);
        parser.parseAll();
    }

    @Test
    void testMixedInput() throws Exception {
        Context ctxt = new Context();
        Calculator calculator = new Calculator();
        RegisterSet registers = new RegisterSet();
        Parser parser = new Parser("4(hi)5.3+", calculator, ctxt, registers);
        parser.parseAll();
        assertEquals(9.3, ctxt.pop()); // 4 + 5.3 = 9.3
        assertEquals("hi", ctxt.pop()); // string should still be on the stack
    }
}