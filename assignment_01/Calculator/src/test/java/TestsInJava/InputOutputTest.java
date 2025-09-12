package TestsInJava;

import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;
import org.streams.InStream;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * this test only works with data stack, input and output stream.
 * no use of command stream
 */
public class InputOutputTest {
    private Calculator calculator;
    private final Context ctxt = new Context();
    private Parser parser;

    @BeforeEach
    public void setUp() {
        ctxt.setTestModeOld(true); // also sets commands stream to only "@"
        calculator = new Calculator(ctxt);
        this.calculator.reset(); // clear data stack
        this.parser = new Parser(calculator);
    }

    @Test
    void testReadLineFromString() {
        // Using input As if the user had typed
        InStream input = new InStream("(Hello World)\n");

        // test with execute command directly
        ctxt.setInputStream(input);
        calculator.executeCommand('\'');
        assertEquals("(Hello World) ▹ @", calculator.getContext().toString());

        // test with execute command directly
        input = new InStream("(Hello World)\n");
        calculator.reset();
        ctxt.setInputStream(input);
        calculator.push("'");
        parser.parseAll();
        assertEquals("(Hello World)", calculator.pop());
    }


    @Test
    void testReadCodeInputFromString() {
        // Using input As if the user had typed
        calculator.push(3);
        InStream input = new InStream("(8)(9~)(4!4$_1+$@)@ \n");
        ctxt.setInputStream(input);
        calculator.executeCommand('\'');
        parser.parseAll();
        assertEquals(8, calculator.pop());
    }

    @Test
    void testReadCodeWithNonASCII() {
        // Using input As if the user had typed
        InStream input = new InStream("äbc(d)");
        ctxt.setInputStream(input);
        Object line = ctxt.readInput();
        assertEquals("bc(d)", line);
    }

    @Test
    void testReadCodeWithNonASCIIPicture() {
        // Using input As if the user had typed
        InStream input = new InStream("café ☕ ");
        ctxt.setInputStream(input);
        Object line = ctxt.readInput();
        assertEquals("caf", line);
    }

    @Test
    void testKeyboardSimulated() {
        String simulatedInput = "42\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes())); // Redirect System.in

        InStream inStream = new InStream();  // Will now read from simulated System.in
        ctxt.setInputStream(inStream);
        Object line = ctxt.readInput();

        assertEquals(42, line);
    }

    @Test
    void testOutput() {
        calculator.push(-3); // negative
        calculator.push(8); // integer
        calculator.push(2.50); // too many decimals
        calculator.push("4!4$_1+$@"); // string
        calculator.push(-3.5670); // string

        int startStackSize = calculator.getContext().getStackSize();

        for (int i = 1; i <= startStackSize; i++) {
            calculator.writeOutput(); // repeat for each stack element
        }

        String output = calculator.getContext().getOutputForTest();
        String expectedOutput = """
                -3.567\
                
                4!4$_1+$@\
                
                2.5\
                
                8\
                
                -3""";

        assertEquals(expectedOutput, output);
    }

    @Test
    void testOutputStackWithOneElement() {
        calculator.getContext().clearCommandStream();
        calculator.push("one");
        calculator.getContext().addToCommandStreamInFront("\"");
        parser.parseAll();
        String output = calculator.getContext().getOutputStream().getTestOuput(true);
        assertEquals("one", output);
    }

    @Test
    void testOutputStackWithFiveElements() {
        System.out.println("Starting test testOutputStackWithFiveElements...");
        calculator.getContext().clearCommandStream();
        calculator.push("five");
        calculator.push("four");
        calculator.push("three");
        calculator.push("two");
        calculator.push("one");
        calculator.getContext().addToCommandStreamInFront("\"\"\"\"\""); // manual version
        parser.parseAll();
        String output = calculator.getContext().getOutputStream().getTestOuput(true);
        assertEquals("one\ntwo\nthree\nfour\nfive", output);
    }

//    @Test
    void testOutputStackWithFiveElementsLoadRegister() {
        System.out.println("Start test testOutputStackWithFiveElementsLoadRegister...");
        calculator.getContext().clearCommandStream();
        calculator.push("five");
        calculator.push("four");
        calculator.push("three");
        calculator.push("two");
        calculator.push("one");

        char letter = (char) ('A' + calculator.getContext().getStackSize() - 1);
        String command = letter + "@";
        calculator.getContext().addToCommandStreamInFront(command);
        Parser parser = new Parser(calculator);
        parser.parseAll();
        String output = calculator.getContext().getOutputStream().getTestOuput(true);
        System.out.println(calculator.getContext().getOutputForTest());
        assertEquals("one\ntwo\nthree\nfour\nfive", output);
    }
}