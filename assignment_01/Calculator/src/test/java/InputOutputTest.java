import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.streams.InStream;
import org.streams.OutputStream;

import java.io.ByteArrayInputStream;
import java.io.Console;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputOutputTest {
    private Calculator calculator;
    private Context ctxt = new Context();

    @BeforeEach
    public void setUp() {
        ctxt.setTestMode(true);
        calculator = new Calculator(ctxt);
        calculator.reset();
    }


    @Test
    void testAddition() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testReadLineFromString() {
        // As if the user had typed: Hello World <Enter>
        InStream input = new InStream("Hello World\n");
        ctxt.setInputStream(input);
        Object line = ctxt.getInputStream().readLine();
        assertEquals("Hello World", line);
    }

    @Test
    void testReadCodeInputFromString() {
        // As if the user had typed: Hello World <Enter>
        InStream input = new InStream("3 (8)(9~)(4!4$_1+$@)@ \n");
        ctxt.setInputStream(input);
        Object line = ctxt.getInputStream().readLine();
        assertEquals("3 (8)(9~)(4!4$_1+$@)@", line);
    }

    @Test
    void testKeyboardSimulated() {
        String simulatedInput = "42\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes())); // Redirect System.in

        InStream inStream = new InStream();  // Will now read from simulated System.in
        ctxt.setInputStream(inStream);
        Object line = ctxt.getInputStream().readLine();

        assertEquals((Integer) 42, line);
    }

    @Test
    void testOutput() {
        calculator.push(-3); // negative
        calculator.push(8); // integer
        calculator.push(2.50); // too many decimals
        calculator.push("4!4$_1+$@"); // string
        calculator.push(-3.5670); // string
        String line1 = ctxt.getOutputStream().write(calculator.pop(), true);
        assertEquals("-3.567", line1);

        String line2 = ctxt.getOutputStream().write(calculator.pop(), true);
        assertEquals("4!4$_1+$@", line2);

        String line3 = ctxt.getOutputStream().write(calculator.pop(),true);
        assertEquals("2.5", line3);

        String line4 = ctxt.getOutputStream().write(calculator.pop(),true);
        assertEquals("8", line4);

        String line5 = ctxt.getOutputStream().write(calculator.pop(), true);
        assertEquals("-3", line5);
    }
}