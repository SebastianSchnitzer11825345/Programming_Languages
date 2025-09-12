import org.calculator.Calculator;
import org.calculator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.streams.IStream;
import org.streams.InStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests adapted in calculator language
 */
public class CalculatorTest {
    Context ctxt = new Context();
    Calculator calculator;

    @BeforeEach
    void initAll() {
        ctxt.setTestMode(true); // set into test (for output) and clear command stream
        calculator = new Calculator(ctxt);
        calculator.reset(); // clears data stack
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3 2 + 5=" // 3 2 + #1=@ 5= "
            , "5 3 - 2="
            , "3 2 * 6="
            , "6 2 / 3="
            , "7 2 % 1=" // 5
            , "1 2.2 + 3.2="
            , "3.2 1 - 2.2="
            , "2.5 4 * 10.0="
            , " (test) 1 + (test1)="
            , "(test) 2 - (te)=" // 10
            , "2 (test) - (st)="
            , "(test) 125 * (test})="
            , "123 (test) * ({test)="
            , "15 3 / 5.0="
            , "15 3 / 5=" // 15
            , "10 2.5 / 4.0="
            , "(another test) (test) / 8="
            , "(no substring) (test) / 1~="
            , "15 4 % 3="
            , "10 2.5 % ()=" // 20
            , "10 0 / ()="
            , "0.1 0.00005 / 2000="
            , "1 0.000005 / ()=" // smaller than EPSILON, thus divide by 0
            , "5 (test) % ()="
            , "(test) 5 % ()=" // 25
            , "(te^t) 2 % 94="
            , "1.05.02 + 1.07="
            , "15 2 3 4 +*- 1="
            , "4 3(2*)@+ 10="
            , "(Hello ) (World)+ 2024+ (Hello World2024)=" // 30
            , "(abcdef)(cd)/ 2="
    })
    void testArithmeticOperations(String command) {
        command += " \" "; // write result to output (expecting 1)
        String result = test(command);
        assertEquals("1", result); // because 3+2 != 5, then 1==1 is true → 1
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2~ (test) - ()="
            ,"(test) 2.2 - ()="
            , "(test) 123.2 * ()="
            , "(test) (another test) * ()="
            , "129 (test) * ()="
            , "1 0 / ()="
    })
    void testArithmeticOperationsNoUsefulSemantics(String command)  {
        command += " \" "; // write result to output (expecting 1)
        String result = test(command);
        assertEquals("1", result); // because 3+2 != 5, then 1==1 is true → 1
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3 2 * 5="
            , "5 6 - 1="
            , "3 2 + 6="
    })
    void testArithmeticOperationsWrong(String command) {
        command += " \" ";
        String result = test(command);
        assertEquals("0", result); // because 3*2 = 6 != 5, then 0==0 is true → 1
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3 *"
            ,"3 2 - +"
    })
    void testDataStackIssue(String command) {
        command += " \" ";
        String result = test(command);
        assertEquals("ERROR", result.substring(0, 5));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2 2 = 1="
            , "2 3 = 0="
            , "3 2 > 1="
            , "2 3 > 0="
            , "2 3 < 1=" // 5
            , "3 2 < 0="
            , "(test) (test) = 1="
            , "(test) (test2) = 0="
            , "1 1.0 = 1="
            , "1 1.1 = 0=" // 10
            , "1 1.00001 = 1=" // within EPSILON range
            , "1 1.0000115 = 0="
            , "4 3+ 10= 0="

    })
    void testComparisonOperations(String command) {
        command += " \" "; // write result to output (expecting 1)
        String result = test(command);
        assertEquals("1", result); // because 3+2 != 5, then 1==1 is true → 1
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2~ 2~="
            , "2~~ 2="
            , "2.2~ 2.2~="
            , "(test)~ ()="
            , "2 2.2 (test) # 3=" // 5
            , "2_ 0="
            , "0 _ 1="
            , "2.0_ 0="
            , "0.0_ 1="
            , "(test)_ 0=" // 10
            , "()_ 1="
            , "2.2? 2="
            , "(test)? ()="
            , "2.2! 2.2="
            , "(test) (placeholder) 3 ! (test)=" // 15
            , "1 1 & 1="
            , "0 1 & 0="
            , "0 0 & 0="
            , "1 1 | 1="
            , "0 1 | 1=" // 20
            , "1 0 | 1="
            , "0 0 | 0="
            , "1.0 1.1 $ # 1="
            , "(test) (test) $ # 1="
            , "3 2 1 $ # 1= @ 3=" // 25
    })
    void testOtherOperations(String command) {
        command += " \" "; // write result to output (expecting 1)
        String result = test(command);
        assertEquals("1", result); // because 3+2 != 5, then 1==1 is true → 1
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3 d@ 6=" // d stands for entire code for factorial
            , "1 (8) (9~) c@ 8=" // c is for if-then
//            , " f@  (cba+52 3a/X)$)=" // enter string analyzer with f directly for tests
    })
    void testsFromAssignment(String command) {
        command += " \" "; // write result to output (expecting 1)
        String result = test(command);
        assertEquals("1", result); // because 3+2 != 5, then 1==1 is true → 1
    }

    @Test
    void testStringAnalyzer() {
        String command =
                "f@ \n"    // line 1
                + "abc+25 a3/X)$";              // line 2
        IStream in = new InStream(command);
        calculator.getContext().setInputStream(in);
        calculator.test();
        String result = calculator.getContext().getOutputForTest();
        System.out.println("result is: " + result);
        String expected =
                    "cba+52 3a/X)$\n" +
                    "Words: 4\n" +
                    "Letters: 5\n" +
                    "Digits: 3\n" +
                    "Spaces: 1\n" +
                    "Special: 4";
        assertEquals(expected, result); // because 3+2 != 5, then 1==1 is true → 1
    }


    /**
     * Helper function for tests
     * @param expr
     */
    String test(String expr)  {
        // Input is still simulated with InStream
        IStream in = new InStream(expr);
        calculator.getContext().setInputStream(in);
        calculator.test();
        return calculator.getContext().getOutputForTest();
    }
}