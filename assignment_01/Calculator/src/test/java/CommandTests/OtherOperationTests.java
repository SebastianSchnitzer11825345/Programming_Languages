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
}
