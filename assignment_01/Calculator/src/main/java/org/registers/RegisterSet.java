package org.registers;

import org.tokens.Token;

import java.util.HashMap;
import java.util.Map;

public class RegisterSet {
    private final Map<Character, Token> registers = new HashMap<>();

    public RegisterSet() {
        initializeRegisters();
    }

    private void initializeRegisters() {
        // A–Z
        for (char c = 'A'; c <= 'Z'; c++) {
            registers.put(c, defaultValue(c));
        }
        // a–z
        for (char c = 'a'; c <= 'z'; c++) {
            registers.put(c, defaultValue(c));
        }
        // Set predefined content of register 'a'
        registers.put('a', new Token("1 2 + PRINT")); // Example command stream
    }

    private Token defaultValue(char name) {
        if (Character.isLowerCase(name)) {
            return new Token(0);
        } else {
            return new Token("");
        }
    }

    public Token read(char name) {
        if (!registers.containsKey(name)) {
            throw new IllegalArgumentException("Invalid register name: " + name);
        }
        return registers.get(name);
    }
}