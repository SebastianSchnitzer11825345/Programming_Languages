import org.calculator.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.streams.InStream;
import org.streams.OutputStream;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputOutputTest {
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
        calculator.reset();
    }


    @Test
    void testAddition() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testReadLineFromString() {
        // As if the user had typed: Hello World <Enter>
        InStream inStream = new InStream("Hello World\n");
        Object line = inStream.readLine();
        assertEquals("Hello World", line);
    }

    @Test
    void testReadCodeInputFromString() {
        // As if the user had typed: Hello World <Enter>
        InStream inStream = new InStream("3 (8)(9~)(4!4$_1+$@)@ \n");
        Object line = inStream.readLine();
        assertEquals("3 (8)(9~)(4!4$_1+$@)@", line);
    }

    @Test
    void testKeyboardSimulated() {
        String simulatedInput = "42\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes())); // Redirect System.in

        InStream inStream = new InStream();  // Will now read from simulated System.in
        Object line = inStream.readLine();

        assertEquals("42", line);
    }

    @Test
    void testOutput() {
        calculator.push(-3); // negative
        calculator.push(8); // integer
        calculator.push(2.50); // too many decimals
        calculator.push("4!4$_1+$@"); // string
        calculator.push(-3.5670); // string
        OutputStream out = new OutputStream();
        StringBuilder line1 = out.write(calculator.pop());
        assertEquals("-3.567", line1.toString());

        StringBuilder line2 = out.write(calculator.pop());
        assertEquals("4!4$_1+$@", line2.toString());

        StringBuilder line3 = out.write(calculator.pop());
        assertEquals("2.5", line3.toString());

        StringBuilder line4 = out.write(calculator.pop());
        assertEquals("8", line4.toString());

        StringBuilder line5 = out.write(calculator.pop());
        assertEquals("-3", line5.toString());
    }
}