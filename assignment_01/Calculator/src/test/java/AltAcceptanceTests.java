import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.ParseException;
import org.parser.Parser;

public class AltAcceptanceTests {
    private Calculator calculator;
    private Context ctxt;

    @BeforeEach
    public void setUp() throws ParseException {
        this.ctxt = new Context();
        this.ctxt.setTestMode(true); // also sets commands stream to only "@"

//        // use for debugging only
//        System.out.println("Command stream is:" + String.valueOf(this.ctxt.getCommandStream()));
//        System.out.println("State is:" + this.ctxt.toString());

        this.calculator = new Calculator(this.ctxt);
        this.calculator.reset();
    }

    @Test
    public void test_factorial3_manual() throws ParseException {
        this.ctxt.push(3); // add what is on stack at start
        String commandFactorialThree = "(A)3!3$3!@2$";
        String C = "4!4$_1+$@";
        String A = "3!3!1-2!1=()5!(C)@2$*";
        A = A.replace("C", C);
        commandFactorialThree = commandFactorialThree.replace("A", A);
        this.ctxt.push(commandFactorialThree);

        // debugging
        System.out.println("generated command is: " + commandFactorialThree);
        System.out.println("State is now:" + this.ctxt.toString());

        Parser parser = new Parser(this.calculator);
        parser.parseAll();
        System.out.println("State at end is: " + this.ctxt.toString());
        Assertions.assertEquals(6, this.calculator.pop());
    }

    @Test
    public void test_if_then_manual() throws ParseException {
        this.ctxt.push(1); // add what is on stack at start
        String ifThen = "(8)(9~)(4!4$_1+$@)@";
        this.ctxt.push(ifThen);

        // debugging
        System.out.println("generated command is: " + ifThen);
        System.out.println("State is now:" + this.ctxt.toString());

        Parser parser = new Parser(this.calculator);
        parser.parseAll();
        // debugging
        System.out.println("State at end is: " + this.ctxt.toString());

        Assertions.assertEquals(8, this.calculator.pop());
    }
}