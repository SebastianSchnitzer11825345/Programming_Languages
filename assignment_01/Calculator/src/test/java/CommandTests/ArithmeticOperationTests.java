package CommandTests;

import org.example.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArithmeticOperationTests {

    private Calculator calculator;


    @BeforeEach
    public void setUp() {
        calculator = new Calculator();

        calculator.reset();
    }

    @Test
    public void test_simpleIntegerAdd() {
        calculator.push(1);
        calculator.push(2);
        calculator.executeCommand('+');

        assertEquals(3, calculator.pop());
    }

    @Test
    public void test_IntegerPlusFloatConvertsToFloat() {
        calculator.push(1);
        calculator.push(2.2);

        calculator.executeCommand('+');

        assertEquals(3.2, calculator.pop());
    }

    @Test
    public void test_StringPlusFloatConvertsToString() {
        calculator.push(1);
        calculator.push("test");

        calculator.executeCommand('+');

        assertEquals("test1", calculator.pop());
    }

    @Test
    public void test_simpleIntegerSubstract() {
        calculator.push(3);
        calculator.push(5);

        calculator.executeCommand('-');

        assertEquals(2, calculator.pop());
    }

    @Test
    public void test_IntegerSubtractFloatConvertsToFloat() {
        calculator.push(1);
        calculator.push(3.2);

        calculator.executeCommand('-');

        assertEquals(2.2, calculator.pop());
    }

    @Test
    public void test_StringSubtractPositiveIntegerRemovesLastChars() {

        calculator.push("test");
        calculator.push(2);

        calculator.executeCommand('-');

        assertEquals("te", calculator.pop());
    }

    @Test
    public void test_IntegerSubtractStringRemovesFirstChars() {
        calculator.push(2);
        calculator.push("test");

        calculator.executeCommand('-');

        assertEquals("st", calculator.pop());
    }

    @Test
    public void test_StringSubtractNegativeIntegerThrowsException() {
        calculator.push("test");
        calculator.push(-2);

        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('-'));
    }

    @Test
    public void test_StringSubtractFloatThrowsException() {
        calculator.push(2.2);
        calculator.push("test");

        assertThrows(IllegalArgumentException.class, () -> calculator.executeCommand('-'));
    }
}
