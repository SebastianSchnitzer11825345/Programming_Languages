package CommandTests;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OtherOperationTests {

    private Calculator calculator;
    private Context ctxt = new Context();

    @BeforeEach
    public void setUp() {
        ctxt.setTestMode(true);
        calculator = new Calculator(ctxt);
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

    @Test
    public void test_CopyFloatDoesNothing() {
        calculator.push(2.2);
        calculator.executeCommand('!');

        assertEquals(2.2, calculator.pop());
    }

    @Test
    public void test_CopyStringDoesNothing() {
        calculator.push("test");
        calculator.executeCommand('!');

        assertEquals("test", calculator.pop());
    }

    @Test
    public void test_CopyInteger_CopiesCorrectElement() {
        calculator.push("test");
        calculator.push("placeholder");
        calculator.push(3);

        calculator.executeCommand('!');
        
        assertEquals("test", calculator.pop());
    }

    @Test
    public void test_LandOn1And1_Returns1() {
        calculator.push(1);
        calculator.push(1);

        calculator.executeCommand('&');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_LandOn0And1_Returns0() {
        calculator.push(0);
        calculator.push(1);

        calculator.executeCommand('&');

        assertEquals(0, calculator.pop());
    }
    @Test
    public void test_LandOn0And0_Returns0() {
        calculator.push(0);
        calculator.push(0);

        calculator.executeCommand('&');

        assertEquals(0, calculator.pop());
    }

    @Test
    public void test_LorOn1And1_Returns1() {
        calculator.push(1);
        calculator.push(1);

        calculator.executeCommand('|');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_LorOn1And0_Returns1() {
        calculator.push(0);
        calculator.push(1);

        calculator.executeCommand('|');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_LorOn0And1_Returns1() {
        calculator.push(1);
        calculator.push(0);

        calculator.executeCommand('|');

        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_LorOn0And0_Returns0() {
        calculator.push(0);
        calculator.push(0);

        calculator.executeCommand('|');

        assertEquals(0, calculator.pop());
    }

    @Test
    public void test_DeleteOnFloat_DoesNothing() {
        calculator.push(1.0);
        calculator.push(1.1);

        calculator.executeCommand('$');

        assertEquals(1, calculator.size());
    }

    @Test
    public void test_DeleteOnString_DoesNothing() {
        calculator.push("test");
        calculator.push("test");

        calculator.executeCommand('$');

        assertEquals(1, calculator.size());
    }

    @Test
    public void test_DeleteOnInteger_RemovesCorrectElement() {
        calculator.push(3);
        calculator.push(2);
        calculator.push(1);


        calculator.executeCommand('$');

        assertAll(
                () -> assertEquals(1, calculator.size()),
                () -> assertEquals(3, calculator.pop())
        );
    }



}
