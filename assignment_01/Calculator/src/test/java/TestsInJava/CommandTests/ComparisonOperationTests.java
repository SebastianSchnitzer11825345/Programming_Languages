package TestsInJava.CommandTests;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparisonOperationTests {

    private Calculator calculator;
    private Context ctxt = new Context();

    @BeforeEach
    public void setUp() {
        ctxt.setTestModeOld(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
    }

    @Test
    public void test_EqualsWithTwoIdenticalIntegers_ReturnsTrue() {
        calculator.push(2);
        calculator.push(2);

        calculator.executeCommand('=');

        assertEquals(1,calculator.pop());
    }

    @Test
    public void test_EqualsWithTwoDifferentIntegers_ReturnsFalse() {
        calculator.push(2);
        calculator.push(3);

        calculator.executeCommand('=');

        assertEquals(0,calculator.pop());
    }

    @Test
    public void test_GreaterThanWithTwoDifferentIntegers_ReturnsTrue() {
        calculator.push(3);
        calculator.push(2);

        calculator.executeCommand('>');

        assertEquals(1,calculator.pop());
    }

    @Test
    public void test_GreaterThanTwoDifferentIntegers_ReturnsFalse() {
        calculator.push(2);
        calculator.push(3);

        calculator.executeCommand('>');

        assertEquals(0,calculator.pop());
    }

    @Test
    public void test_LessThanTwoDifferentIntegers_ReturnsFalse() {
        calculator.push(2);
        calculator.push(3);

        calculator.executeCommand('<');

        assertEquals(1,calculator.pop());
    }

    @Test
    public void test_LessThanTwoDifferentIntegers_ReturnsTrue() {
        calculator.push(3);
        calculator.push(2);

        calculator.executeCommand('<');

        assertEquals(0,calculator.pop());
    }

    @Test
    public void test_WhenComparingTwoIdenticalStrings_ReturnsTrue() {
        calculator.push("test");
        calculator.push("test");

        calculator.executeCommand('=');

        assertEquals(1,calculator.pop());
    }

    @Test
    public void test_whenComparingTwoDifferentStrings_ReturnsFalse() {
        calculator.push("test");
        calculator.push("test2");

        calculator.executeCommand('=');

        assertEquals(0,calculator.pop());
    }

    @Test
    public void test_whenCompareIntegerToFloat_ConvertsCorrectlyAndReturnsTrue() {
        calculator.push(1);
        calculator.push(1.0);

        calculator.executeCommand('=');

        assertEquals(1,calculator.pop());
    }

    @Test
    public void test_whenCompareIntegerToFloat_ConvertsCorrectlyAndReturnsFalse() {
        calculator.push(1);
        calculator.push(1.1);

        calculator.executeCommand('=');

        assertEquals(0,calculator.pop());
    }

    @Test
    public void test_whenCompareIntegerToFloatWithinEpsilon_ConvertsCorrectlyAndReturnsTrue() {
        calculator.push(1);
        calculator.push(1+Calculator.EPSILON);

        calculator.executeCommand('=');

        assertEquals(1,calculator.pop());
    }

    @Test
    public void test_whenCompareIntegerToFloatNotWithinEpsilon_ConvertsCorrectlyAndReturnsFalse() {
        calculator.push(1);
        calculator.push(1+(Calculator.EPSILON*1.1));

        calculator.executeCommand('=');

        assertEquals(0,calculator.pop());
    }
}
