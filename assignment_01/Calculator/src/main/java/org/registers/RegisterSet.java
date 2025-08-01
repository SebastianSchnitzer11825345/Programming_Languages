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

        // b–z, as a already taken
        for (char c = 'e'; c <= 'z'; c++) {
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
        contents.append("8)(9~)(4!4$_1+$@)@");
        return contents.toString();
    }

    /**
     * Testing factorial recursive loop from exercise
     * @return String
     */
    private String reg_d_factorial() {
        String factorial_Input = "(A)3!3$3!@2$";
        String C = "4!4$_1+$@";
        String A = "3!3!1-2!1=()5!(C)@2$*";
        A = A.replace("C",C);
        return factorial_Input.replace("A",A);
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