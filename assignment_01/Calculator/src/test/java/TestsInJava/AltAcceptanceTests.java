package TestsInJava;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;

public class AltAcceptanceTests {
    private Calculator calculator;
    private Context ctxt;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.ctxt = new Context();
        this.ctxt.setTestModeOld(true); // also sets commands stream to only "@"

        this.calculator = new Calculator(this.ctxt);
        this.calculator.reset(); // clear data stack
        this.parser = new Parser(calculator);
    }

    @Test
    public void test_factorial3_manual() {
        this.ctxt.push(3); // add what is on stack at start
        String commandFactorialThree = "(A)3!3$3!@2$";
        String C = "4!4$_1+$@";
        String A = "3!3!1-2!1=()5!(C)@2$*";
        A = A.replace("C", C);
        commandFactorialThree = commandFactorialThree.replace("A", A);
        this.ctxt.push(commandFactorialThree);

        parser.parseAll();
        Assertions.assertEquals(6, this.calculator.pop());
    }

    @Test
    public void test_if_then_manual() {
        this.ctxt.push(1); // add what is on stack at start
        String ifThen = "(8)(9~)(4!4$_1+$@)@";
        this.ctxt.push(ifThen);
        parser.parseAll();

        Assertions.assertEquals(8, this.calculator.pop());
    }
}