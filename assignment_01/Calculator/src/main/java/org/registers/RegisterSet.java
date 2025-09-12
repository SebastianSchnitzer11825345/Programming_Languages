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
        return "(Welcome to calculator)" +
                '"' +
                "(Enter expression and press enter, h@ for help) " +
                '"' +
                '\'' +  // READ input from User and  add to data stack
                '@' +   // EXECute command from top of data stack
                '\"' +  // command to OUTput top element on data stack
                "(Would you like to continue (enter 1) or exit (enter 0) )" +
                '"' +

                // start of if-then-else loop
                '\'' + // READ input from User and  add to data stack
                "(b)" + // if true continue by calling b
                "(x)" + // else exit
                "c@@"; // if-then-condition
    }

    /**
     * use b for second call without another welcome message
     * @return String content of b register
     */
    private String reg_b_loop() {
        return "(Enter expression and press enter) " +
                '"' +
                '\'' + // READ input from User and  add to data stack
                '@' + // EXECute command from top of data stack
                '\"' + // command to OUTput top element on data stack

                // start of if-then-else loop
                "(Cont (1) |exit (0) )" +
                '"' +
                // start of if-then-else loop
                '\'' + // READ input from User and  add to data stack
                "(b)" + // if true continue by calling b
                "(x)" + // else exit
                "c@@"; // execute if-then-condition and option
    }

    /**
     * Testing boolean from exercise
     * on stack should be "if not true (else)", "if true" and "boolean" in this order from top to down
     * @return String
     */
    private String reg_c_if_then() {
        return "(4!4$_1+$@)@";
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
        return "(Enter string to analyse) " +
                '"' +
                '\'' + //Read input string
                "0 " + // starting index
                "0 0 0 0 0 " + // Counters for word, letter, digit, space, special characters
                "()" + // output string
                "()" + // currentWord (for word reversing)
                "A@"; // jump to main loop (before f@)
    }

    /**
     * Press f@
     * String to analyse from exercise is "abc+25 a3/X)$"
     * @return String
     */
    private String reg_f_stringAnalyser_from_tests() {
        return '\'' + //Read input string
                "0 " + // starting index
                "0 0 0 0 0 " + // Counters for word, letter, digit, space, special characters
                "()" + // output string
                "()" + // currentWord (for word reversing)
                "A@"; // jump to main loop (before f@)
    }

    private String reg_h_help() {
        return "(Calculator uses specific syntax: )\"" +
                "(Options: )\"" +
                "(i: Enter command using post-notation and syntax rules (e.g. '3 2 +' will output 6))\"" +
                "( Accepted types: Integer, Decimal Place, String) \"" +
                "( Allowed operators: =, <, >, +, -, *, /, %, &, |, _, ~, !, $, @, \\, #, ', \" ) \"" +
                "( String constructions with ( String ) ) \"" +
                "( Calling functions in registers directly with a-z, A-Z) \"" +
                "( ) \"" +
                "(ii: Use string analyzer by entering command 's @' and you will enter the string in next step)";
    }

    private String reg_s_callStringAnalyzer() {
        return "(e)@@";
    }

    private String reg_t_testMode() {
        return "'" + // READ input from User and  add to data stack
                '@'; // EXECute command from top of data stack
    }

    private String reg_A_analyse_loop() {
        return "W@" + //get char at index from input string
                "()=" + // check for end of string
                "(O)" + // if yes, go to output
                "(B)" +  // if no, call classifier functions: letter, digit, space, special char
                "c@@"; // execute if - then, then execute option
    }

    private String reg_B_classify_cap_letter() {
        // 65-90 --> check if capital letter (include both 65 and 90)
        return  "W@" + // get Ascii code of letter
                "91<" + // include 90
                "Y@" + // get Ascii code of letter (2nd step)
                "64>" + // include 65
                "&" + // both conditions met for capital letter
                "(F)" + // if yes, go to letter counter
                "(C)" + // if not, continue classifying
                "c@@"; // execute if-then + run result
    }

    private String reg_C_classify_small_letter() {
        // 97-122 --> check if small caps letter (include both 97 and 122)
        return "W@" + // get Ascii code of letter
                "123<" + // include 122
                "Y@" + // get Ascii code of letter (2nd step)
                "96>" + // include 97
                "&" + // both conditions met for small caps letter
                "(F)" + // if yes, go to letter counter
                "(D)" + // if not, continue classifying
                "c@@"; // execute if-then + run result
    }

    private String reg_D_classify_digit() {
        //48-57 --> Digit
        return "W@" + // get Ascii code of letter
                "47>" + // include 48
                "Y@" + // get Ascii code of letter (2nd step)
                "58<" +
                "&" + // both conditions met
                "(G)" + // if yes, go to digit counter
                "(E)" + // if not, continue classifying
                "c@@"; // execute if-then + run result
    }

    private String reg_E_classify_space() {
        // 32 --> Space . check for space
        return "W@" + // get Ascii code of letter
                "32=" + // check if space
                "(T@ (I) (H) c@)" + // if yes, go to space counter (via T check for word, 0 for word)
                "(T@ (K) (J) c@)" + // if no, go to special counter (via T check for word, 0 for word)
                "c@@"; // execute if-then + run result
    }

    private String reg_F_letter() {
        // update current word first, by reversing the characters directly
        return "W@" + // get ASCII for char at index from input string
                "3!*" + // copy currentWord, append char in beginning
                "2$" + // replace old current word
                // update and reorder entire stack
                "V@" + // index++, delete old entry on stack
                "U@" + // rearrange stack: counter words
                "V@" + // counter letters++, delete old entry on stack
                "U@U@U@U@U@" + // rearrange stack: digit, space, special character, output string, currentWord
                "A@"; // loop back
    }

    private String reg_G_digit() {
        // update current word first, by reversing the characters directly
        return "W@" + // get ASCII for char at index from input string
                "3!*" + // copy currentWord, append char in beginning
                "2$" + // replace old current word

                // update and reorder entire stack
                "V@" + // index++, delete old entry on stack
                "U@U@" + // rearrange stack: counter words, counter letters
                "V@" + // counter digits++, delete old entry on stack
                "U@U@U@U@" + // rearrange stack: space, special character, output string, currentWord

                "A@"; // loop back
    }

    private String reg_H_space_with_word() {
        // update current output string first
        return "X@" + // update Output String and reset current word to ""

                // update and reorder entire stack
                "V@" + // index++, delete old entry on stack
                "V@" + // counter words++, delete old entry on stack
                "U@U@" + // rearrange stack: counter letters, counter digits
                "V@" + // counter space++, delete old entry on stack
                "U@U@U@" + // rearrange stack: counter special character, updated output string, new currentWord

                "A@"; // loop back
    }

    private String reg_I_space_no_word() {
        // update current output string first
        return "W@2$*()" + // get ASCII for char at index from input string, add it to output string

                // update and reorder entire stack
                "V@" + // index++, delete old entry on stack
                "U@U@U@" + // rearrange stack: counter words, letters, digits
                "V@" + // counter space++, delete old entry on stack
                "U@U@U@" + // rearrange stack: counter special character, updated output string, new currentWord

                "A@"; // loop back
    }

    private String reg_J_special_with_word() {
        // update current output string first
        return "X@" + // update Output String and reset current word to ""

                // update and reorder entire stack
                "V@" + // index++, delete old entry on stack
                "V@" + // counter words++, delete old entry on stack
                "U@U@U@" + // rearrange stack: counter letters, digits, space
                "V@" + // counter special character++, delete old entry on stack
                "U@U@" + // rearrange stack: updated output string, new currentWord

                "A@"; // loop back
    }

    private String reg_K_special_no_word() {
        // update current output string first
        return "W@2$*()" + // get ASCII for char at index from input string, add it to output string

                // update and reorder entire stack
                "V@" + // index++, delete old entry on stack
                "U@U@U@U@" + // rearrange stack: counter words, letters, digits, space
                "V@" + // counter special character++, delete old entry on stack
                "U@U@" + // rearrange stack: updated output string, new currentWord

                "A@"; // loop back
    }

    private String reg_L_CounterUpdate() {
        // update current output string first
        return "Z@" + // update Output String and reset current word to ""

                // update and reorder entire stack
                "U@" + // rearrange stack: index
                "V@" + // counter words++, delete old entry on stack
                "U@U@U@U@U@U@"; // rearrange stack: counters letters, digits, space, special, updated output string, new currentWord
    }

    private String reg_O_finalOutput() {
        return "T@ () (L@) c@" + // if there is word, go to space counter (via T check for word, 0 for word)
                "3!\"" + // Output String
                "(Words: )9!+\"" +
                "(Letters: )8!+\" " +
                "(Digits: )7!+\" " +
                "(Spaces: )6!+\" " +
                "(Special: )5!+\"" +
                // clean data stack for further use (9 elements on it)
                "1$1$1$1$1$1$1$1$?"; // last one just replaced with () so sth stays on stack
    }

    private String reg_T_checkIfCurrentWord() {
        return "2!" +// copy current word
                "_";// check if any current word (with _ pops 1 if no word, 0 if word is there
    }
    private String reg_U_stackupdate() {
        return "9!9$"; // update stack during letter update
    }

    private String reg_V_stackincrement() {
        return "9!1+9$"; // update stack and increment value ++
    }

    private String reg_W_charAscii() {
        return "10!10!%"; // get string, then index, then capture ASCII integer value of char
    }

    private String reg_X_updateOutputStringIntermediate() {
        return '+' + // update output string with current word
                "()" + // update currentWord
                "W@2$*()"; // get ASCII for char at index from input string, add it to output string
    }

    private String reg_Y_charAscii_two_step() {
        return "11!11!%"; // get string, then index, then capture ASCII integer value of char
    }

    private String reg_Z_updateOutputStringFinal() {
        return '+' + // update output string with current word
                "()"; // update currentWord
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