import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.ParseException;
import org.parser.Parser;
import org.streams.InStream;

import static org.calculator.Main.run;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptanceTests {

    private Calculator calculator;
    private Context ctxt;
    private Parser parser;


    @BeforeEach
    public void setUp() throws ParseException {
        this.ctxt= new Context();
        ctxt.setTestMode(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
        calculator.getContext().clearCommandStream();
        this.parser = new Parser(calculator);
    }

    // If this test fails, something is wrong with the testing environment
    @Test
    public void simplePositiveTest() throws ParseException {
        calculator.getContext().addToCommandStreamInFront("3 5 +");
        Parser parser = new Parser(calculator);

        parser.parseAll();

        assertEquals(8, calculator.pop());
    }

    @Test
    public void test_fromAssignment1() throws ParseException {
        calculator.getContext().addToCommandStreamInFront("(8)(9~)(4!4$_1+$@)@");
        Parser parser = new Parser(calculator);

        parser.parseAll();

        assertEquals(8, calculator.pop());
    }

    @Test
    public void test_fromAssignment2() throws ParseException {
        calculator.getContext().addToCommandStreamInFront("3(A)3!3$3!@2$");
        Parser parser = new Parser(calculator);

        parser.parseAll();


        assertEquals(3, calculator.pop());
    }
}
