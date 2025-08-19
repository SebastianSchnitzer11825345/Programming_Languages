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
        temp.put('n', reg_n_callStringAnalyzer());

        // o–s, as a-m already taken
        for (char c = 'o'; c <= 'z'; c++) {
            if (c != 't' && c != 'x') {
                temp.put(c, defaultValue(c));
            }
        }

        temp.put('t', reg_t_testMode());

        temp.put('x', "+"); // cause it to terminate due to error

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
        contents.append('\'');  // READ input from User and  add to data stack
        contents.append('@');   // EXECute command from top of data stack
        contents.append('\"');  // command to OUTput top element on data stack
        contents.append("(Would you like to continue (enter 1) or exit (enter 0) )");
        contents.append('"');
        // start of if-then-else loop
        contents.append('\''); // READ input from User and  add to data stack
        contents.append("(b)"); // if true continue by calling b
        contents.append("(x)"); // else exit
        contents.append("c@@"); // if-then-condition
//        contents.append("@");

//        // TODO: old code, delete later
//        contents.append("(b)"); // move to LOOP in b
//        contents.append('@');   // call b again to START LOOP

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

        // start of if-then-else loop
        contents.append("(Would you like to continue (enter 1) or exit (enter 0) )");
        contents.append('"');
        // start of if-then-else loop
        contents.append('\''); // READ input from User and  add to data stack
        contents.append("(b)"); // if true continue by calling b
        contents.append("(x)"); // else exit
        contents.append("c"); // if-then-condition
        contents.append("@"); // if-then-condition
        return contents.toString();
    }

    /**
     * Testing boolean from exercise
     * @return String
     */
    private String reg_c_if_then() {
        StringBuilder contents = new StringBuilder();
        contents.append("(4!4$_1+$@)@");
//        // TODO: old code, delete later
//        contents.append("1 "); // starting stack (1 is true)
//        contents.append("(8)(9~)(4!4$_1+$@)@");
        return contents.toString();
    }

    /**
     * Testing factorial recursive loop from exercise (factorial of 3)
     * @return String
     */
    private String reg_d_factorial() {
        String contents = "(A)3!3$3!@2$";
        String C = "4!4$_1+$@";
        String A = "3!3!1-2!1=()5!(C)@2$*";
        A = A.replace("C",C);
        return contents.replace("A",A);
    }

    /**
     * Press e@
     * @return
     */
    private String reg_e_stringAnalyser() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Enter string to analyse) ");
        contents.append('"');
        contents.append('\''); //Read input string
//        contents.append('@');
        contents.append("0 "); // starting index
        contents.append("0 0 0 0 0 "); //Counters for word, letter, digit, space, special characters
        contents.append("0 "); //currentWord (for word reversing)
        contents.append("()"); // output string
        contents.append('f'); // jump to main loop
        contents.append('@');
        contents.append(')');

        return contents.toString();
    }

    private String reg_f_analyse_loop() {
        StringBuilder contents = new StringBuilder();

        contents.append("9!"); //copy index
        contents.append("11!11!%"); //get char at index from input string


        contents.append("()="); // check for end of string

        contents.append("(g)"); // if yes, go to output
        contents.append("(_)"); // if no, continue
        contents.append("c@"); // execute if - then

        // old code
//        contents.append("(g)@"); // if yes, go to output
//        contents.append("_"); // if no, continue

        contents.append("10!"); //Copy char index again (now at another index)
        contents.append("(h)@@"); // Call helper function, pushes category: 0=letter, 1=digit, 2=space, 3=special

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
        contents.append("12!12!%"); //get char at index from input string (first copy entire word, then index, then call index) and Ascii code of letter
        //65-90, 97-122 --> Letter
        contents.append("64<"); // include 65
//        contents.append("1"); // not letter
        contents.append("13!13!%"); //get char at index from input string (first copy entire word, then index, then call index) and Ascii code of letter
        contents.append("96>"); // include 97
//        contents.append("1|"); //
        contents.append("|"); // either small or large capital letter
        contents.append("_"); // 0 for letter, 1 if not


        //48-57 --> Digit
        contents.append("13!13!%"); //get char at index from input string (first copy entire word, then index, then call index)
        contents.append("47>"); // include 48
        contents.append("14!14!%"); //get char at index from input string (first copy entire word, then index, then call index)
        contents.append("58<");
        contents.append("&"); // both conditions met
        // TODO: here not yet working, if digit it should replace 0 with 1, otherwise keep 1, but also continue with 2 for next step... or maybe do this later
//        contents.append("&"); // if 1 there from before (not letter), then capture 1 for Digit
        contents.append("_1+"); // 1 -> 0 -> 1, 0 -> 1 -> 2
//        contents.append("1&");
//        contents.append("1_");

        // 32 --> Space
        contents.append("2!"); // stack 2 2 or 1 1 or 0 0
        contents.append("2=+"); // stack 2 1 --> 3 or 1 0 --> 1 or 0 0 --> 0
        contents.append("14!14!"); //get char at index from input string (first copy entire word, then index, then call index)
        contents.append("%"); //Get Ascii code of letter
        contents.append("32="); // 1 if equal (we have stack .... 3 1 (should be 2) or ... 3 0 (should be 3) or ... 1 0 (should be 1) or ... 0 0 (should be 0)
        contents.append("-"); // 3 for Space
//        contents.append("2&"); // does not work, there is as both are considered positive not compared 2=2

        // Otherwise Special character (not further code required as 3 already set in previous step
//        contents.append("15!15!"); //get char at index from input string (first copy entire word, then index, then call index)
//        contents.append("%"); //Get Ascii code of letter
//        contents.append("3|");

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

    private String reg_n_callStringAnalyzer() {
        StringBuilder contents = new StringBuilder();
        contents.append("e");
        return contents.toString();
    }

    private String reg_t_testMode() {
        StringBuilder contents = new StringBuilder();
        contents.append('\''); // READ input from User and  add to data stack
        contents.append('@'); // EXECute command from top of data stack
//        contents.append('\"'); // command to OUTput top element on data stack
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