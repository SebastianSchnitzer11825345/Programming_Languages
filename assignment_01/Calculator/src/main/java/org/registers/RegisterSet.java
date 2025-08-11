package org.registers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterSet implements IReadOnlyRegisters {
    private final Map<Character, Object> registers; // read-only

    public RegisterSet() {
        Map<Character, Object> temp = new HashMap<>();
        initializeRegisters(temp);

        // Wrap in unmodifiable map
        this.registers = Collections.unmodifiableMap(temp);
    }

    private void initializeRegisters(Map<Character, Object> temp) {
        // Set predefined content of register 'a'
        temp.put('a',reg_a_welcome());

        temp.put('b',reg_b_loop());

        temp.put('c',reg_c_if_then());

        temp.put('d', reg_d_factorial());

        temp.put('e', reg_e_stringAnalyser());

        temp.put('f', reg_f_analyse_loop());

        temp.put('g', reg_g_finalOutput());
        temp.put('h', reg_h_classifier());
        temp.put('i', reg_i_flushWord());
        temp.put('j', reg_j_letter());
        temp.put('k', reg_k_digit());
        temp.put('l', reg_l_space());
        temp.put('m', reg_m_special());

        // b–z, as a already taken
        for (char c = 'n'; c <= 'z'; c++) {
            temp.put(c, defaultValue(c));
        }

        // Populate A–K with increasing numbers of quote characters to print up to 10 elements of data stack
        for (char c = 'A'; c <= 'A' + 10; c++) {
            temp.put(c, "\"".repeat((c - 'A') + 1));
        }

        // L–Z
        for (char c = 'A' + 11; c <= 'Z'; c++) {
            temp.put(c, defaultValue(c));
        }
    }

    private String reg_a_welcome() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Welcome to calculator)");
        contents.append('"');
        contents.append("(Enter expression and press enter) ");
        contents.append('"');
        contents.append('\''); // READ input from User and  add to data stack
        contents.append('@'); // EXECute command from top of data stack
        contents.append('\"'); // command to OUTput top element on data stack
        contents.append('b'); // LOOP back to a
        contents.append('@'); // call b again to RESTART
        return contents.toString();
    }

    /**
     * use b for second call without another welcome message
     * @return String content of b register
     */
    private String reg_b_loop() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Enter expression and press enter) ");
        contents.append('"');
        contents.append('\''); // READ input from User and  add to data stack
        contents.append('@'); // EXECute command from top of data stack
        contents.append('\"'); // command to OUTput top element on data stack
        contents.append('b');
        contents.append('@'); // call b again to RESTART
        return contents.toString();
    }

    /**
     * Testing boolean from exercise
     * @return String
     */
    private String reg_c_if_then() {
        StringBuilder contents = new StringBuilder();
        contents.append("1 "); // starting stack (1 is true)
        contents.append("(8)(9~)(4!4$_1+$@)@");
        return contents.toString();
    }

    /**
     * Testing factorial recursive loop from exercise (factorial of 3)
     * @return String
     */
    private String reg_d_factorial() {
        String factorial_Input = "(A)3!3$3!@2$";
        String C = "4!4$_1+$@";
        String A = "3!3!1-2!1=()5!(C)@2$*";
        A = A.replace("C",C);
        return factorial_Input.replace("A",A);
    }

    private String reg_e_stringAnalyser() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Enter string to analyse) ");
        contents.append('"');
        contents.append('\''); //Read input string
        contents.append('@');
        contents.append('0'); // starting index
        contents.append("0 0 0 0 0"); //Counters for word, letter, digit, space, special characters
        contents.append("0"); //currentWord (for word reversing)
        contents.append("()"); // output string
        contents.append('f'); // jump tp main loop
        contents.append('@');

        return contents.toString();
    }

    private String reg_f_analyse_loop() {
        StringBuilder contents = new StringBuilder();

        contents.append("9!"); //copy index
        contents.append("10!%"); //get char at index from input string


        contents.append("()="); // check for end of string
        contents.append("(g)@"); // if yes, go to output
        contents.append("_"); // if no, continue

        contents.append("9!"); //Copy char again
        contents.append("(h)@"); // Call helper function, pushes category: 0=letter, 1=digit, 2=space, 3=special

        contents.append("3!"); // Check if char is a letter
        contents.append("0=");
        contents.append("(f_j)@");
        contents.append("_");

        contents.append("3!"); //Check if char is a digit
        contents.append("1=");
        contents.append("(f_k)@");
        contents.append("_");

        contents.append("3!"); //Check if char is space
        contents.append("2=");
        contents.append("(f_l)@");
        contents.append("_");

        contents.append("(f_m)@"); // Else is special character

        return contents.toString();
    }

    private String reg_g_finalOutput() {
        StringBuilder contents = new StringBuilder();

        contents.append("(i)@"); //Flush last output

        contents.append("10!\"");

        contents.append("(Words: )7!\"");
        contents.append("(Letters: )6!\" ");
        contents.append("(Digits: )5!\" ");
        contents.append("(Spaces: )4!\" ");
        contents.append("(Special: )3!\"");

        return contents.toString();
    }

    private String reg_h_classifier() {
        StringBuilder contents = new StringBuilder();

        contents.append("%"); //Get Ascii code of letter

        //65-90, 97-122 --> Letter
        contents.append("65>"); //
        contents.append("1"); // not letter
        contents.append("97<");
        contents.append("1|"); //
        contents.append("0_");


        //48-57 --> Digit
        contents.append("48<");
        contents.append("57>");
        contents.append("&");
        contents.append("1&");
        contents.append("1_");

        // 32 --> Space
        contents.append("32=");
        contents.append("2&");

        // Otherwise Special character
        contents.append("3|");

        return contents.toString();
    }

    private String reg_i_flushWord() {
        StringBuilder contents = new StringBuilder();

        contents.append("8!"); // get word
        contents.append("()=");
        contents.append("_"); //if word is not empty, continue

        contents.append("1");
        contents.append("7!1+");
        contents.append("$");

        contents.append("8!"); //get word
        contents.append("~"); //reverse string
        contents.append("10!+"); // append to output
        contents.append("$");

        contents.append("()");
        contents.append("$");

        return contents.toString();
    }

    private String reg_j_letter() {
        StringBuilder contents = new StringBuilder();

        contents.append("8!"); //copy word
        contents.append("9!+"); //append char to word
        contents.append("$"); // replace old word

        contents.append("6!1+"); // letters++
        contents.append("$");

        contents.append("2!1+"); // index++
        contents.append("$");

        contents.append("(f)@"); // loop back

        return contents.toString();
    }

    private String reg_k_digit() {
        StringBuilder contents = new StringBuilder();

        contents.append("8!"); //copy word
        contents.append("9!+"); //append char to word
        contents.append("$"); // replace old word

        contents.append("5!1+"); // digit++
        contents.append("$");

        contents.append("2!1+"); // index++
        contents.append("$");

        contents.append("(f)@"); // loop back

        return contents.toString();
    }

    private String reg_l_space() {
        StringBuilder contents = new StringBuilder();

        contents.append("(i)@"); //flush word

        contents.append("4!1+"); // space++
        contents.append("$");

        contents.append("2!1+"); // index++
        contents.append("$");

        contents.append("10!9!+"); //output + space
        contents.append("$");

        contents.append("(f)@"); // loop back

        return contents.toString();
    }

    private String reg_m_special() {
        StringBuilder contents = new StringBuilder();

        contents.append("(i)@"); //flush word

        contents.append("3!1+"); // special++
        contents.append("$");

        contents.append("2!1+"); // index++
        contents.append("$");

        contents.append("10!9!+"); //output + space
        contents.append("$");

        contents.append("(f)@"); // loop back

        return contents.toString();
    }





    private Object defaultValue(char name) {
        if (Character.isLowerCase(name)) {
            return 0;
        } else {
            return "";
        }
    }

    public Object read(char name) {
        if (!registers.containsKey(name)) {
            throw new IllegalArgumentException("Invalid register name: " + name);
        }
        return registers.get(name);
    }
}