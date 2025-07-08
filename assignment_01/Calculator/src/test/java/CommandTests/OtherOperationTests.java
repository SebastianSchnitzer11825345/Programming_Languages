package CommandTests;

import org.example.Calculator;
import org.junit.jupiter.api.BeforeEach;

public class OtherOperationTests {

    private Calculator calculator;


    @BeforeEach
    public void setUp() {
        calculator = new Calculator();

        calculator.reset();
    }
}
