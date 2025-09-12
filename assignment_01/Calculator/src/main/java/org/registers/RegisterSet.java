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

        // helper functions
        temp.put('b',reg_b_loop());
        temp.put('c',reg_c_if_then());
        temp.put('d', reg_d_factorial());
        temp.put('e', reg_e_stringAnalyser());
        temp.put('f', reg_f_stringAnalyser_from_tests());

        temp.put('h',reg_h_help());
        temp.put('s', reg_s_callStringAnalyzer());
        temp.put('t', reg_t_testMode());
        temp.put('x', ":"); // cause it to terminate due to error

        // the free small caps
        for (char c = 'g'; c <= 'z'; c++) {
            if (c != 'h' && c != 's' && c != 't' && c != 'x') {
                temp.put(c, defaultValue(c));
            }
        }

        // use capital letters for functions required by String Analyzer
        temp.put('A', reg_A_analyse_loop());
        temp.put('B', reg_B_classify_cap_letter());
        temp.put('C', reg_C_classify_small_letter());
        temp.put('D', reg_D_classify_digit());
        temp.put('E', reg_E_classify_space());
        temp.put('F', reg_F_letter());
        temp.put('G', reg_G_digit());
        temp.put('H', reg_H_space_with_word());
        temp.put('I', reg_I_space_no_word());
        temp.put('J', reg_J_special_with_word());
        temp.put('K', reg_K_special_no_word());
        temp.put('L', reg_L_CounterUpdate());

        temp.put('O', reg_O_finalOutput());

        // helper functions for String analyser
        temp.put('T', reg_T_checkIfCurrentWord());
        temp.put('U', reg_U_stackupdate());
        temp.put('V', reg_V_stackincrement());
        temp.put('W', reg_W_charAscii());
        temp.put('X', reg_X_updateOutputStringIntermediate());
        temp.put('Y', reg_Y_charAscii_two_step());
        temp.put('Z', reg_Z_updateOutputStringFinal());

        // Populate A–K with increasing numbers of quote characters to print up to 10 elements of data stack
        for (char c = 'M'; c <= 'S'; c++) {
            if (c != 'O') {
                temp.put(c, defaultValue(c));
            }
        }
    }

    private String reg_a_welcome() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Welcome to calculator)");
        contents.append('"');
        contents.append("(Enter expression and press enter, h@ for help) ");
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
        contents.append("c@@"); // execute if-then-condition and option
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
     * @return String
     */
    private String reg_e_stringAnalyser() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Enter string to analyse) ");
        contents.append('"');
        contents.append('\''); //Read input string
        contents.append("0 "); // starting index
        contents.append("0 0 0 0 0 "); // Counters for word, letter, digit, space, special characters
        contents.append("()"); // output string
        contents.append("()"); // currentWord (for word reversing)
        contents.append("A@"); // jump to main loop (before f@)

        return contents.toString();
    }

    /**
     * Press f@
     * String to analyse from exercise is "abc+25 a3/X)$"
     * @return String
     */
    private String reg_f_stringAnalyser_from_tests() {
        String contents = '\'' + //Read input string
                "0 " + // starting index
                "0 0 0 0 0 " + // Counters for word, letter, digit, space, special characters
                "()" + // output string
                "()" + // currentWord (for word reversing)
                "A@"; // jump to main loop (before f@)

        return contents;
    }

    private String reg_h_help() {
        String contents = "(Calculator uses specific syntax: )\"" +
                "(Options: )\"" +
                "(i: Enter command using post-notation and syntax rules (e.g. '3 2 +' will output 6))\"" +
                "( Accepted types: Integer, Decimal Place, String) \"" +
                "( Allowed operators: =, <, >, +, -, *, /, %, &, |, _, ~, !, $, @, \\, #, \', \" ) \"" +
                "( String constructions with ( String ) ) \"" +
                "( Calling functions in registers directly with a-z, A-Z) \"" +
                "( ) \"" +
                "(ii: Use string analyzer by entering command 's @' and you will enter the string in next step)";

        return contents;
    }

    private String reg_s_callStringAnalyzer() {
        StringBuilder contents = new StringBuilder();
        contents.append("(e)@@");
        return contents.toString();
    }

    private String reg_t_testMode() {
        StringBuilder contents = new StringBuilder();
        contents.append('\''); // READ input from User and  add to data stack
        contents.append('@'); // EXECute command from top of data stack
        return contents.toString();
    }

    private String reg_A_analyse_loop() {
        StringBuilder contents = new StringBuilder();

        contents.append("W@"); //get char at index from input string
        contents.append("()="); // check for end of string
        contents.append("(O)"); // if yes, go to output
        contents.append("(B)");  // if no, call classifier functions: letter, digit, space, special char
        contents.append("c@@"); // execute if - then, then execute option

        return contents.toString();
    }

    private String reg_B_classify_cap_letter() {
        StringBuilder contents = new StringBuilder();
        // 65-90 --> check if capital letter (include both 65 and 90)
        contents.append("W@"); // get Ascii code of letter
        contents.append("91<"); // include 90
        contents.append("Y@"); // get Ascii code of letter (2nd step)
        contents.append("64>"); // include 65
        contents.append("&"); // both conditions met for capital letter
        contents.append("(F)"); // if yes, go to letter counter
        contents.append("(C)"); // if not, continue classifying
        contents.append("c@@"); // execute if-then + run result
        return contents.toString();
    }

    private String reg_C_classify_small_letter() {
        StringBuilder contents = new StringBuilder();
        // 97-122 --> check if small caps letter (include both 97 and 122)
        contents.append("W@"); // get Ascii code of letter
        contents.append("123<"); // include 122
        contents.append("Y@"); // get Ascii code of letter (2nd step)
        contents.append("96>"); // include 97
        contents.append("&"); // both conditions met for small caps letter
        contents.append("(F)"); // if yes, go to letter counter
        contents.append("(D)"); // if not, continue classifying
        contents.append("c@@"); // execute if-then + run result
        return contents.toString();
    }

    private String reg_D_classify_digit() {
        StringBuilder contents = new StringBuilder();
        //48-57 --> Digit
        contents.append("W@"); // get Ascii code of letter
        contents.append("47>"); // include 48
        contents.append("Y@"); // get Ascii code of letter (2nd step)
        contents.append("58<");
        contents.append("&"); // both conditions met
        contents.append("(G)"); // if yes, go to digit counter
        contents.append("(E)"); // if not, continue classifying
        contents.append("c@@"); // execute if-then + run result
        return contents.toString();
    }

    private String reg_E_classify_space() {
        StringBuilder contents = new StringBuilder();
        // 32 --> Space . check for space
        contents.append("W@"); // get Ascii code of letter
        contents.append("32="); // check if space
        contents.append("(T@ (I) (H) c@)"); // if yes, go to space counter (via T check for word, 0 for word)
        contents.append("(T@ (K) (J) c@)"); // if no, go to special counter (via T check for word, 0 for word)
        contents.append("c@@"); // execute if-then + run result
        return contents.toString();
    }

    private String reg_F_letter() {
        StringBuilder contents = new StringBuilder();

        // update current word first, by reversing the characters directly
        contents.append("W@"); // get ASCII for char at index from input string
        contents.append("3!*"); // copy currentWord, append char in beginning
        contents.append("2$"); // replace old current word

        // update and reorder entire stack
        contents.append("V@"); // index++, delete old entry on stack
        contents.append("U@"); // rearrange stack: counter words
        contents.append("V@"); // counter letters++, delete old entry on stack
        contents.append("U@U@U@U@U@"); // rearrange stack: digit, space, special character, output string, currentWord

        contents.append("A@"); // loop back
        return contents.toString();
    }

    private String reg_G_digit() {
        StringBuilder contents = new StringBuilder();

        // update current word first, by reversing the characters directly
        contents.append("W@"); // get ASCII for char at index from input string
        contents.append("3!*"); // copy currentWord, append char in beginning
        contents.append("2$"); // replace old current word

        // update and reorder entire stack
        contents.append("V@"); // index++, delete old entry on stack
        contents.append("U@U@"); // rearrange stack: counter words, counter letters
        contents.append("V@"); // counter digits++, delete old entry on stack
        contents.append("U@U@U@U@"); // rearrange stack: space, special character, output string, currentWord

        contents.append("A@"); // loop back
        return contents.toString();
    }

    private String reg_H_space_with_word() {
        StringBuilder contents = new StringBuilder();

        // update current output string first
        contents.append("X@"); // update Output String and reset current word to ""

        // update and reorder entire stack
        contents.append("V@"); // index++, delete old entry on stack
        contents.append("V@"); // counter words++, delete old entry on stack
        contents.append("U@U@"); // rearrange stack: counter letters, counter digits
        contents.append("V@"); // counter space++, delete old entry on stack
        contents.append("U@U@U@"); // rearrange stack: counter special character, updated output string, new currentWord

        contents.append("A@"); // loop back
        return contents.toString();
    }

    private String reg_I_space_no_word() {
        StringBuilder contents = new StringBuilder();

        // update current output string first
        contents.append("W@2$*()"); // get ASCII for char at index from input string, add it to output string

        // update and reorder entire stack
        contents.append("V@"); // index++, delete old entry on stack
        contents.append("U@U@U@"); // rearrange stack: counter words, letters, digits
        contents.append("V@"); // counter space++, delete old entry on stack
        contents.append("U@U@U@"); // rearrange stack: counter special character, updated output string, new currentWord

        contents.append("A@"); // loop back
        return contents.toString();
    }

    private String reg_J_special_with_word() {
        StringBuilder contents = new StringBuilder();

        // update current output string first
        contents.append("X@"); // update Output String and reset current word to ""

        // update and reorder entire stack
        contents.append("V@"); // index++, delete old entry on stack
        contents.append("V@"); // counter words++, delete old entry on stack
        contents.append("U@U@U@"); // rearrange stack: counter letters, digits, space
        contents.append("V@"); // counter special character++, delete old entry on stack
        contents.append("U@U@"); // rearrange stack: updated output string, new currentWord

        contents.append("A@"); // loop back
        return contents.toString();
    }

    private String reg_K_special_no_word() {
        StringBuilder contents = new StringBuilder();

        // update current output string first
        contents.append("W@2$*()"); // get ASCII for char at index from input string, add it to output string

        // update and reorder entire stack
        contents.append("V@"); // index++, delete old entry on stack
        contents.append("U@U@U@U@"); // rearrange stack: counter words, letters, digits, space
        contents.append("V@"); // counter special character++, delete old entry on stack
        contents.append("U@U@"); // rearrange stack: updated output string, new currentWord

        contents.append("A@"); // loop back
        return contents.toString();
    }

    private String reg_L_CounterUpdate() {
        StringBuilder contents = new StringBuilder();

        // update current output string first
        contents.append("Z@"); // update Output String and reset current word to ""

        // update and reorder entire stack
        contents.append("U@"); // rearrange stack: index
        contents.append("V@"); // counter words++, delete old entry on stack
        contents.append("U@U@U@U@U@U@"); // rearrange stack: counters letters, digits, space, special, updated output string, new currentWord

        return contents.toString();
    }

    private String reg_O_finalOutput() {
        StringBuilder contents = new StringBuilder();
        contents.append("T@ () (L@) c@"); // if there is word, go to space counter (via T check for word, 0 for word)
        contents.append("3!\""); // Output String
        contents.append("(Words: )9!+\"");
        contents.append("(Letters: )8!+\" ");
        contents.append("(Digits: )7!+\" ");
        contents.append("(Spaces: )6!+\" ");
        contents.append("(Special: )5!+\"");
        // clean data stack for further use (9 elements on it)
        contents.append("1$1$1$1$1$1$1$1$?"); // last one just replaced with () so sth stays on stack

        return contents.toString();
    }

    private String reg_T_checkIfCurrentWord() {
        StringBuilder contents = new StringBuilder();
        contents.append("2!");// copy current word
        contents.append("_");// check if any current word (with _ pops 1 if no word, 0 if word is there
        return contents.toString();
    }
    private String reg_U_stackupdate() {
        StringBuilder contents = new StringBuilder();
        contents.append("9!9$"); // update stack during letter update
        return contents.toString();
    }

    private String reg_V_stackincrement() {
        StringBuilder contents = new StringBuilder();
        contents.append("9!1+9$"); // update stack and increment value ++
        return contents.toString();
    }

    private String reg_W_charAscii() {
        StringBuilder contents = new StringBuilder();
        contents.append("10!10!%"); // get string, then index, then capture ASCII integer value of char
        return contents.toString();
    }

    private String reg_X_updateOutputStringIntermediate() {
        StringBuilder contents = new StringBuilder();
        contents.append('+'); // update output string with current word
        contents.append("()"); // update currentWord
        contents.append("W@2$*()"); // get ASCII for char at index from input string, add it to output string
        return contents.toString();
    }

    private String reg_Y_charAscii_two_step() {
        StringBuilder contents = new StringBuilder();
        contents.append("11!11!%"); // get string, then index, then capture ASCII integer value of char
        return contents.toString();
    }

    private String reg_Z_updateOutputStringFinal() {
        StringBuilder contents = new StringBuilder();
        contents.append('+'); // update output string with current word
        contents.append("()"); // update currentWord
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