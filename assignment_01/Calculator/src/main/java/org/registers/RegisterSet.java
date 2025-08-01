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
        temp.put('a',reg_a_contents());

        // b–z, as a already taken
        for (char c = 'b'; c <= 'z'; c++) {
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

    private String reg_a_contents() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Welcome to calculator)");
        contents.append('"');
        contents.append("(Enter expression and press enter) ");
        contents.append('"');
        contents.append('\''); // READ input from User and  add to data stack
        contents.append('@'); // EXECute command from top of data stack
        contents.append('#'); // push size of data stack on top
        // TODO: how figure out how to call the right register based on the stack size
        contents.append('b'); // call register b to output data stack
        // LOOP
        return contents.toString();
    }

    /**
     * use b for calling stack output
     * limit data stack to 10 elements
     * @return String content of b register
     */
    private String reg_b_contents() {
//        return "\"\"\"\"\"";
        return "\"".repeat(10); // limit size of stack to 10
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