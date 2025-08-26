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
            if (c != 't' && c != 'x' && c != 'u' && c != 'v' && c != 'w') {
                temp.put(c, defaultValue(c));
            }
        }

        temp.put('t', reg_t_testMode());
        temp.put('u', reg_u_stackupdate());
        temp.put('v', reg_v_stackincrement());
        temp.put('w', reg_w_charAscii());

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
        contents.append("(Cont (1) |exit (0) )");
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
     * on stack should be "if not true (else)", "if true" and "boolean" in this order from top to down
     * @return String
     */
    private String reg_c_if_then() {
        StringBuilder contents = new StringBuilder();
        contents.append("(4!4$_1+$@)@");
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
     * String to analyse from exercise is "abc+25 a3/X)$"
     * @return
     */
    private String reg_e_stringAnalyser() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Enter string to analyse) ");
        contents.append('"');
        contents.append('\''); //Read input string
//        contents.append('@');
        contents.append("0 "); // starting index
        contents.append("0 0 0 0 0 "); // Counters for word, letter, digit, space, special characters
        contents.append("0 "); // output string
        contents.append("()"); // currentWord (for word reversing)
        contents.append("f@"); // jump to main loop (before f@)
        contents.append(':');

        return contents.toString();
    }

    private String reg_f_analyse_loop() {
        StringBuilder contents = new StringBuilder();

        contents.append("9!"); //copy index
        contents.append("11!3!%"); //get char at index from input string

        // TODO: check if this is the best way to solve check for end of string
        contents.append("()="); // check for end of string
        contents.append("2$"); // clean up index from top of stack
        contents.append("(g)"); // if yes, go to output
        contents.append("(h)");  // if no, call helper function, pushes category: 0=letter, 2=digit, 3=space, 1=special

        contents.append("c@"); // execute if - then
        contents.append('@'); // execute either if or else option

        // TODO: remove the checks and add the perform directly in classifier when identifying what it is
        contents.append("2!0="); // Check if char is a letter
        contents.append("(j)()c@@"); // if letter, perform j for letter... if not letter, do nothing, next check... execute if - then

        contents.append("2!2="); //Check if char is a digit
        contents.append("(k)()c@@"); // if digit, perform k for digit... if not digit, do nothing, next check... execute if - then

        contents.append("2!3="); //Check if char is a white space
        contents.append("(l)()c@@"); // if space, perform l for white space... if not space, do nothing, next check... execute if - then

        contents.append("2!1="); //Check if char is special character
        contents.append("(m)()c@@"); // if special character, perform m for white space... if not special char, do nothing, next check... execute if - then

        return contents.toString();
    }

    private String reg_g_finalOutput() {
        StringBuilder contents = new StringBuilder();

        contents.append("i@"); //Flush last output

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

        // TODO: simplify by going straight to update stack after identifying what it is
        // 65-90 --> check if capital letter (include both 65 and 90)
        contents.append("w@"); //get char at index from input string (first copy entire word, then index, then call index) and Ascii code of letter
        contents.append("64<"); // include 65
        contents.append("w@"); //get char at index from input string (first copy entire word, then index, then call index) and Ascii code of letter
        contents.append("91>"); // include 90
        contents.append("&"); // both conditions met for capital letter

        // 97-122 --> check if small caps letter (include both 97 and 122)
        contents.append("12!4!%"); //get char at index from input string (first copy entire word, then index, then call index) and Ascii code of letter
        contents.append("123<"); // include 122
        contents.append("13!5!%"); //get char at index from input string (first copy entire word, then index, then call index) and Ascii code of letter
        contents.append("96>"); // include 97
        contents.append("&"); // both conditions met for small caps letter
        contents.append("|"); // either small or large capital letter
        contents.append("_"); // 0 for letter, 1 if not

        //48-57 --> Digit
        contents.append("12!3!%"); //get char at index from input string (first copy entire word, then index, then call index)
        contents.append("47>"); // include 48
        contents.append("13!4!%"); //get char at index from input string (first copy entire word, then index, then call index)
        contents.append("58<");
        contents.append("&"); // both conditions met
        contents.append("(1+)()c@"); // c (if then else) -> if digit, change 1 to 2, else leave at 1

        // 32 --> Space . check for space
        contents.append("12!3!"); //get char at index from input string (first copy entire word, then index, then call index)
        contents.append("%"); //Get Ascii code of letter
        contents.append("32="); // 1 if equal (we have stack .... 1 1 (Space) or ... 0 0 (letter) or ... 1 0 (digit) or ... 1 0 (other character)
        contents.append("(2+)()c@"); // c (if then else) -> if 1 change to 2, else 0

        contents.append("2$"); // clean curr_index from 2nd position on top of stack

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

        contents.append("8!"); // get word
        contents.append("~"); // reverse string
        contents.append("10!+"); // append to output
        contents.append("$");

        contents.append("()");
        contents.append("$");

        return contents.toString();
    }

    private String reg_j_letter() {
        StringBuilder contents = new StringBuilder();
        contents.append('@'); // pop classifier integer from stack

        // update current word first, by reversing the characters directly
        contents.append("10!10!%"); // get ASCII for char at index from input string
        contents.append("4!"); // copy currentWord
        contents.append("*"); // append char to currentWord copy in beginning
        contents.append("2$"); // replace old current word

        // update and reorder entire stack
        contents.append("v@"); // index++, delete old entry on stack
        contents.append("u@"); // rearrange stack: word
        contents.append("v@"); // letters++, delete old entry on stack
        contents.append("u@u@u@u@u@"); // rearrange stack: digit, space, special character, output string, currentWord

        contents.append("f@"); // loop back to f

        return contents.toString();
    }

    private String reg_k_digit() {
        StringBuilder contents = new StringBuilder();

        contents.append('@'); // pop classifier integer from stack

        // update current word first, by reversing the characters directly
        contents.append("10!10!%"); // get ASCII for char at index from input string
        contents.append("4!"); // copy currentWord
        contents.append("*"); // append char to currentWord copy in beginning
        contents.append("2$"); // replace old current word

        // update and reorder entire stack
        contents.append("v@"); // index++, delete old entry on stack
        contents.append("u@"); // rearrange stack: word
        contents.append("v@"); // letters++, delete old entry on stack
        contents.append("u@u@u@u@u@"); // rearrange stack: digit, space, special character, output string, currentWord

        contents.append("f@"); // loop back to f

        // original from here
//        contents.append("8!"); //copy word
//        contents.append("9!+"); //append char to word
//        contents.append("$"); // replace old word
//
//        contents.append("5!1+"); // digit++
//        contents.append("$");
//
//        contents.append("2!1+"); // index++
//        contents.append("$");
//
//        contents.append("(f)@"); // loop back
        // original to here

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

    private String reg_u_stackupdate() {
        StringBuilder contents = new StringBuilder();
        contents.append("9!9$"); // update stack during letter update
        return contents.toString();
    }

    private String reg_v_stackincrement() {
        StringBuilder contents = new StringBuilder();
        contents.append("9!1+9$"); // update stack and increment value ++
        return contents.toString();
    }

    private String reg_w_charAscii() {
        StringBuilder contents = new StringBuilder();
        contents.append("11!11!%"); // get string, then index, then capture ASCII integer value of char
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