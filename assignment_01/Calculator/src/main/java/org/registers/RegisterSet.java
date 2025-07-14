package org.registers;

import org.tokens.Token;

import java.util.HashMap;
import java.util.Map;

public class RegisterSet {
    private final Map<Character, Object> registers = new HashMap<>();

    public RegisterSet() {
        initializeRegisters();
    }

    private void initializeRegisters() {
        // A–Z
        for (char c = 'A'; c <= 'Z'; c++) {
            registers.put(c, defaultValue(c));
        }
        // a–z
        for (char c = 'b'; c <= 'z'; c++) {
            registers.put(c, defaultValue(c));
        }
        // Set predefined content of register 'a'

        registers.put('a',reg_a_contents());
    }

    private StringBuilder reg_a_contents() {
        StringBuilder contents = new StringBuilder();
        contents.append("(Welcome to calculator)");
        contents.append('"');
        contents.append("(Enter expression and press enter) ");
        contents.append('"');
        contents.append('\''); // READ input from User and add to data stack
        contents.append('@'); // EXECute command from top of data stack

        // OUTput STACK
        // LOOP
        return contents;
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