import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.Parser;
import org.streams.InStream;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * this test only works with data stack, input and output stream.
 * no use of command stream
 */
public class InputOutputTest {
    private Calculator calculator;
    private Context ctxt;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.ctxt = new Context();
        ctxt.setTestMode(true); // also sets commands stream to only "@"
        calculator = new Calculator(ctxt);
        this.calculator.reset(); // clear data stack
//        calculator.push("\'"); // for reading input stream
        this.parser = new Parser(calculator);
    }

//    @Test
//    void testReadLineFromString() throws Exception {
//        // Using input As if the user had typed
//        InStream input = new InStream("(Hello World)\n");
//
//        // test with execute comand directly
//        ctxt.setInputStream(input);
//        calculator.executeCommand('\'');
//        assertEquals("(Hello World) ▹ @", calculator.getContext().toString());
//
//        // test with execute comand directly
//        input = new InStream("(Hello World)\n");
//        calculator.reset();
//        ctxt.setInputStream(input);
//        calculator.push("'");
//        System.out.println("Current state is: " + calculator.getContext().toString());
//        System.out.println("Current Instream is: " + calculator.getContext().getInputStream().toString());
//        parser.parseAll();
//        assertEquals("(Hello World)", calculator.pop());
//    }

//
//    @Test
//    void testReadCodeInputFromString() {
//        // Using input As if the user had typed
//        calculator.push(3);
//        InStream input = new InStream("(8)(9~)(4!4$_1+$@)@ \n");
//        ctxt.setInputStream(input);
//        calculator.executeCommand("'");
//        parser.parseAll();
//        assertEquals("3 (8)(9~)(4!4$_1+$@)@", line);
//    }
//
//    @Test
//    void testReadCodeWithNonASCII() {
//        // Using input As if the user had typed
//        InStream input = new InStream("äbc(d)");
//        ctxt.setInputStream(input);
//        Object line = ctxt.readInput();
//        assertEquals("bc(d)", line);
//    }
//
//    @Test
//    void testReadCodeWithNonASCIIPicture() {
//        // Using input As if the user had typed
//        InStream input = new InStream("café ☕ ");
//        ctxt.setInputStream(input);
//        Object line = ctxt.readInput();
//        assertEquals("caf", line);
//    }
//
//    @Test
//    void testKeyboardSimulated() {
//        String simulatedInput = "42\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes())); // Redirect System.in
//
//        InStream inStream = new InStream();  // Will now read from simulated System.in
//        ctxt.setInputStream(inStream);
//        Object line = ctxt.readInput();
//
//        assertEquals(42, line);
//    }
//
//    @Test
//    void testOutput() {
//        calculator.push(-3); // negative
//        calculator.push(8); // integer
//        calculator.push(2.50); // too many decimals
//        calculator.push("4!4$_1+$@"); // string
//        calculator.push(-3.5670); // string
//
//        int startStackSize = calculator.getContext().getStackSize();
//
//        for (int i = 1; i <= startStackSize; i++) {
//            calculator.writeOutput(); // repeat for each stack element
//        }
//
//        String output = calculator.getContext().getOutputForTest();
//        StringBuilder expectedOutput = new StringBuilder();
//        expectedOutput.append("-3.567");
//        expectedOutput.append("\n");
//        expectedOutput.append("4!4$_1+$@");
//        expectedOutput.append("\n");
//        expectedOutput.append("2.5");
//        expectedOutput.append("\n");
//        expectedOutput.append("8");
//        expectedOutput.append("\n");
//        expectedOutput.append("-3");
//
//        assertEquals(expectedOutput.toString(), output);
//
//    }
//
//    @Test
//    void testOutputStackWithOneElement() throws Exception {
//        calculator.getContext().clearCommandStream();
//        calculator.push("one");
//        calculator.getContext().addToCommandStreamInFront("\"");
//        Parser parser = new Parser(calculator);
//        parser.parseAll();
//        String output = calculator.getContext().getOutputStream().getTestOuput(true);
//        assertEquals("one", output);
//    }
//
//    @Test
//    void testOutputStackWithFiveElements() throws Exception {
//        System.out.println("Starting test testOutputStackWithFiveElements...");
//        calculator.getContext().clearCommandStream();
//        calculator.push("five");
//        calculator.push("four");
//        calculator.push("three");
//        calculator.push("two");
//        calculator.push("one");
//        calculator.getContext().addToCommandStreamInFront("\"\"\"\"\""); // manual version
//        Parser parser = new Parser(calculator);
//        parser.parseAll();
//        String output = calculator.getContext().getOutputStream().getTestOuput(true);
//        assertEquals("one\ntwo\nthree\nfour\nfive", output);
//    }
//
//    @Test
//    void testOutputStackWithFiveElementsLoadRegister() throws Exception {
//        System.out.println("Start test testOutputStackWithFiveElementsLoadRegister...");
//        calculator.getContext().clearCommandStream();
//        calculator.push("five");
//        calculator.push("four");
//        calculator.push("three");
//        calculator.push("two");
//        calculator.push("one");
//
//        // TODO: delete later, just for debugging
////        Stack<Object> datastack = calculator.getContext().getDataStack();
////        System.out.println("Printing current data stack of size " + calculator.getContext().getStackSize());
////        for (Object object : datastack) {
////            System.out.println(object);
////        }
//
//        char letter = (char) ('A' + calculator.getContext().getStackSize() - 1);
//        String command = letter + "@";
//        calculator.getContext().addToCommandStreamInFront(command);
////        System.out.println("Current command stream is: " + calculator.getContext().getCommandStream());
//        Parser parser = new Parser(calculator);
//        parser.parseAll();
////        System.out.println("Current command stream is: " + calculator.getContext().getCommandStream());
//        String output = calculator.getContext().getOutputStream().getTestOuput(true);
//        // just debugging, delete later
////        System.out.println("Output is :" + output);
//        System.out.println(calculator.getContext().getOutputForTest());
//        assertEquals("one\ntwo\nthree\nfour\nfive", output);
//    }

}