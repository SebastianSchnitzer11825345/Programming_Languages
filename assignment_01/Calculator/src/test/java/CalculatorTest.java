//import org.calculator.Calculator;
//import org.calculator.Context;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.parser.ParseException;
//import org.streams.InStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Tests adapted in calculator language
// */
//public class CalculatorTest {
//    Context ctxt = new Context();
//    Calculator calculator = new Calculator(ctxt);
//
//    @BeforeEach
//    public void setUp() {
//        this.calculator.reset(); // clear data stack
//    }
//
//    @Test
//    public void test_operators() {
//        interpretAndExpect("15 2 3 4+*-\n", 1);
//    }
//
//    @Test
//    void testIntegerParsing()  {
//        // 3. Provide simulated user input: first line is `3 5 +`, then `4 6 +`
//        String simulatedInput = "3 5 +\n4 6 +\n";
////        InStream input = new InStream(simulatedInput);
//        ctxt.setInputStream(new InStream(simulatedInput));
//
//        interpretAndExpect("123", 123);
//    }
//
//    public void interpretAndExpect(String expr, Object expected) {
//        try {
//            calculator.getContext().setInputStream(new InStream(expr));
//            calculator.test();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            fail();
//            return;
//        }
//        assertEquals(calculator.pop() , expected);
//    }
//}