//package CommandTests;
//
//import org.calculator.Calculator;
//import org.calculator.Context;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.Console;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class ArithmeticOperationTests {
//
//    private Calculator calculator;
//    private Context ctxt = new Context();
//
//    @BeforeEach
//    public void setUp() {
//        ctxt.setTestMode(true);
//        calculator = new Calculator(ctxt);
//        calculator.reset();
//    }
//
//    @Test
//    public void test_simpleIntegerAdd() {
//        calculator.push(1);
//        calculator.push(2);
//        calculator.executeCommand('+');
//
//        assertEquals(3, calculator.pop());
//    }
//
//    @Test
//    public void test_IntegerPlusFloatConvertsToFloat() {
//        calculator.push(1);
//        calculator.push(2.2);
//
//        calculator.executeCommand('+');
//
//        assertEquals(3.2, calculator.pop());
//    }
//
//    @Test
//    public void test_StringPlusFloatConvertsToString() {
//        calculator.push("test");
//        calculator.push(1);
//
//        calculator.executeCommand('+');
//
//        assertEquals("test1", calculator.pop());
//    }
//
//    @Test
//    public void test_simpleIntegerSubstract() {
//        calculator.push(5);
//        calculator.push(3);
//
//        calculator.executeCommand('-');
//
//        assertEquals(2, calculator.pop());
//    }
//
//    @Test
//    public void test_IntegerSubtractFloatConvertsToFloat() {
//        calculator.push(3.2);
//        calculator.push(1);
//
//        calculator.executeCommand('-');
//
//        assertEquals(2.2, calculator.pop());
//    }
//
//    @Test
//    public void test_StringSubtractPositiveIntegerRemovesLastChars() {
//
//        calculator.push(2);
//        calculator.push("test");
//
//        calculator.executeCommand('-');
//
//        assertEquals("te", calculator.pop());
//    }
//
//    @Test
//    public void test_IntegerSubtractStringRemovesFirstChars() {
//        calculator.push("test");
//        calculator.push(2);
//
//        calculator.executeCommand('-');
//
//        assertEquals("st", calculator.pop());
//    }
//
//    @Test
//    public void test_StringSubtractNegativeIntegerThrowsException() {
//        calculator.push(-2);
//        calculator.push("test");
//
//        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('-'));
//    }
//
//    @Test
//    public void test_StringSubtractFloatThrowsException() {
//        calculator.push("test");
//        calculator.push(2.2);
//
//        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('-'));
//    }
//
//    @Test
//    public void test_simpleIntegerMultiply() {
//        calculator.push(5);
//        calculator.push(3);
//
//        calculator.executeCommand('*');
//
//        assertEquals(15, calculator.pop());
//    }
//
//    @Test
//    public void test_MultiplyFloatWithInteger() {
//        calculator.push(2.5);
//        calculator.push(4);
//
//        calculator.executeCommand('*');
//
//        assertEquals(10.0, calculator.pop());
//    }
//
//    @Test
//    public void test_MultiplyStringWithInteger_AddsCharAtEnd() {
//        calculator.push("test");
//        calculator.push(125);
//
//        calculator.executeCommand('*');
//
//        assertEquals("test}", calculator.pop());
//    }
//
//    @Test
//    public void test_MultiplyIntegerWithString_AddsCharAtBeginning() {
//        calculator.push(123);
//        calculator.push("test");
//
//        calculator.executeCommand('*');
//
//        assertEquals("{test", calculator.pop());
//    }
//
//    @Test
//    public void test_MultiplyFloatWithString_ThrowsIllegalArgumentException() {
//        calculator.push("test");
//        calculator.push(123.2);
//
//        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('*'));
//    }
//
//    @Test
//    public void test_MultiplyStringWithString_ThrowsIllegalArgumentException() {
//        calculator.push("test");
//        calculator.push("another test");
//
//        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('*'));
//    }
//
//    @Test
//    public void test_MultiplyIntegerGreaterThan128WithString_ThrowsIllegalArgumentException() {
//        calculator.push(128);
//        calculator.push("test");
//
//        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('*'));
//    }
//
//    @Test
//    public void test_DivideTwoIntegers_YieldsCorrectResult() {
//        calculator.push(15);
//        calculator.push(3);
//
//        calculator.executeCommand('/');
//
//        assertEquals(5.0, calculator.pop());
//    }
//
//    @Test
//    public void test_DivideFloatAndInteger_YieldsCorrectResult() {
//        calculator.push(10);
//        calculator.push(2.5);
//
//        calculator.executeCommand('/');
//        assertEquals(4.0, calculator.pop());
//    }
//
//    @Test
//    public void test_DivideByZero_ThrowsException() {
//        calculator.push(1);
//        calculator.push(0);
//
//        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('/'));
//    }
//
//    @Test
//    public void test_DivideByString_ReturnsCorrectIndex() {
//        calculator.push("another test");
//        calculator.push("test");
//
//        calculator.executeCommand('/');
//
//        assertEquals(8, calculator.pop());
//    }
//
//    @Test
//    public void test_DivideTwoDifferentStrings_ReturnsMinus1() {
//        calculator.push("no substring");
//        calculator.push("test");
//
//        calculator.executeCommand('/');
//
//        assertEquals(-1, calculator.pop());
//    }
//
//    @Test
//    public void test_ModuloOnTwoIntegers_ReturnsModulo() {
//        calculator.push(15);
//        calculator.push(4);
//
//        calculator.executeCommand('%');
//
//        assertEquals(3, calculator.pop());
//    }
//
//    @Test
//    public void test_ModuloOnFloat_ReturnsEmptyString() {
//        calculator.push(10);
//        calculator.push(2.0);
//        calculator.executeCommand('%');
//
//        assertEquals("()", calculator.pop());
//    }
//
//    @Test
//    public void test_ModuloOnSemanticError_ReturnsEmptyString() {
//        calculator.push(5);
//        calculator.push("test");
//
//        calculator.executeCommand('%');
//
//        assertEquals("()", calculator.pop());
//    }
//
//    @Test
//    public void test_ModuloOnStringAndInteger_ReturnsCorrectAsciiCharacter() {
//        calculator.push("te^t");
//        calculator.push(2); // ^ is 94 in Ascii
//
//        calculator.executeCommand('%');
//
//        assertEquals(94, calculator.pop());
//    }
//}
