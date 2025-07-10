package org.registers;

import java.util.HashMap;
import java.util.Map;

public class RegisterSet {
    private final Map<Character, RegisterValue> registers = new HashMap<>();

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
        registers.put('a', new RegisterValue("1 2 + PRINT")); // Example command stream
    }

    private RegisterValue defaultValue(char name) {
        // Provide sensible defaults (could be overwritten later)
        if (Character.isLowerCase(name)) {
            return new RegisterValue(0);  // or ""
        } else {
            return new RegisterValue(0);
        }
    }

    public RegisterValue read(char name) {
        if (!registers.containsKey(name)) {
            throw new IllegalArgumentException("Invalid register name: " + name);
        }
        return registers.get(name);
    }
}