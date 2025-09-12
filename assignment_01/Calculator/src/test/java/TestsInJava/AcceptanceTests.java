package TestsInJava;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptanceTests {

    private Calculator calculator;
    private final Context ctxt =  new Context();
    private Parser parser;


    @BeforeEach
    public void setUp() {
        ctxt.setTestMode(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
        calculator.getContext().clearCommandStream();
        this.parser = new Parser(calculator);
    }

    @Test
    public void simplePositiveTest() {
        calculator.getContext().addToCommandStreamInFront("3");
        parser.parseAll();
        assertEquals(3, calculator.pop());
    }

    @Test
    public void test_fromAssignment1() {
        calculator.getContext().addToCommandStreamInFront("1(8)(9~)(4!4$_1+$@)@");
        parser.parseAll();
        assertEquals(8, calculator.pop());
    }

    @Test
    public void test_simpleAdditionWithSpace_WorksCorrectly() {
        calculator.getContext().addToCommandStreamInFront("5.1 12.3 +");
        parser.parseAll();
        assertEquals(17.4, calculator.pop());
    }

    @Test
    public void test_simpleAdditionWithoutSpace_ThrowsEmptyStackException() {
        calculator.getContext().addToCommandStreamInFront("12 +");
        parser.parseAll();
        assertThrows(EmptyStackException.class, () -> calculator.pop());
    }

    @Test
    public void test_arithmeticChain_ReturnsCorrectResult() {
        calculator.getContext().addToCommandStreamInFront("15 2 3 4 +*-");
        parser.parseAll();
        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_arithmeticWithApplyImmediately_ReturnsCorrectResult() {
        calculator.getContext().addToCommandStreamInFront("4 3(2*)@+");
        parser.parseAll();
        assertEquals(10, calculator.pop());
    }

    @Test
    public void test_Equal() {
        calculator.getContext().addToCommandStreamInFront("5 5=");
        parser.parseAll();
        assertEquals(1, calculator.pop());
    }

    @Test
    public void test_EqualAndPrint() {
        calculator.getContext().addToCommandStreamInFront("4 3+ 10= ");
        parser.parseAll();
        assertEquals(0, calculator.pop());
    }

    @Test
    public void test_StringAddition() {
        calculator.getContext().addToCommandStreamInFront("(Hello ) (World)+ 2024+ ");
        parser.parseAll();
        assertEquals("Hello World2024", calculator.pop());
    }

    @Test
    public void test_StringIndexing() {
        calculator.getContext().addToCommandStreamInFront("(abcdef)(cd)/ ");
        parser.parseAll();
        assertEquals(2, calculator.pop());
    }
}