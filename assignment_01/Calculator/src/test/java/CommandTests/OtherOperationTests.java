package CommandTests;

import org.example.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherOperationTests {

    private Calculator calculator;


    @BeforeEach
    public void setUp() {
        calculator = new Calculator();

        calculator.reset();
    }

    @Test
    public void test_NegationOnPositiveInteger_ReturnsNegativeInteger() {
        calculator.push(2);

        calculator.executeCommand('~');

        assertEquals(-2, calculator.pop());
    }

    @Test
    public void test_NegationOnNegativeInteger_ReturnsPositiveInteger() {
        calculator.push(-2);

        calculator.executeCommand('~');

        assertEquals(2, calculator.pop());
    }

    @Test
    public void test_NegationOnPositiveDouble_ReturnsNegativeDouble() {
        calculator.push(2.2);

        calculator.executeCommand('~');

        assertEquals(-2.2, calculator.pop());
    }

    @Test
    public void test_NegationOnString_ReturnsEmptyString() {
        calculator.push("test");

        calculator.executeCommand('~');

        assertEquals("()", calculator.pop());
    }

    @Test
    public void test_StackSize() {
        calculator.push(2);
        calculator.push(2.2);
        calculator.push("test");

        calculator.executeCommand('#');
        assertEquals(3, calculator.pop());
    }

    @Test
    public void test_NullCheckOnNonNullInteger_Returns0() {
        calculator.push(2);

        calculator.executeCommand('_');

        assertEquals(0, calculator.pop());
    }

    @Test
    public void test_NullCheckOnNullInteger_Returns1() {
        calculator.push(0);

        calculator.executeCommand('_');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_NullCheckOnNonNullFloat_Returns0() {
        calculator.push(2.0);

        calculator.executeCommand('_');

        assertEquals(0, calculator.pop());
    }

    @Test
    public void test_NullCheckOnNullFloat_Returns1() {
        calculator.push(0.0);

        calculator.executeCommand('_');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_NullCheckOnNonNullString_Returns0() {
        calculator.push("test");

        calculator.executeCommand('_');

        assertEquals(0, calculator.pop());
    }

    @Test
    public void test_NullCheckOnEmptyString_Returns1() {
        calculator.push("()");

        calculator.executeCommand('_');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_IntegerConversionOnFloat_ConvertsToInteger() {
        calculator.push(2.2);

        calculator.executeCommand('?');

        assertEquals(2, calculator.pop());
    }

    @Test
    public void test_IntegerConversionOnInteger_ReturnsEmptyString() {
        calculator.push(2);

        calculator.executeCommand('?');

        assertEquals("()", calculator.pop());
    }

    @Test
    public void test_IntegerConversionOnString_ReturnsEmptyString() {
        calculator.push("test");

        calculator.executeCommand('?');

        assertEquals("()", calculator.pop());
    }
}
