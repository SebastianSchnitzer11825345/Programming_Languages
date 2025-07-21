package org.parser;
// TODO: add the analyzer as test - see description from Aufgabe
/**
 * Write program code (accessible through registers) to modify and
 * analyze strings typed in at run time. Each character in a string is
 * classified as either letter, digit, white-space character or special char-
 * acter. Each longest possible character sequence in a string consisting
 * only of letters and digits is a word. The program output consists of
 * • the input string where each occurring word is reversed,
 * • and the numbers of words, letters, digits, white-space char-
 * acters and special characters in the string (for each categorie
 * counted separately).
 * For example, if the input is abc+25 a3/X)$ (without enclosing paren-
 * theses), the output string shall be cba+52 3a/X)$. This string con-
 * tains 4 words,5 letters,3 digits,1 white-space character and 3 special
 * characters. Please use the data stack to hold all data needed in the
 * computation.
 */
public class StringAnalyzer {
    private int words;
    private int letters;
    private int whiteSpaceChar;
    private int specialChar;

    public StringAnalyzer() {}


    private void parseToken(Object o) {
//        if (o instanceof Integer) {
//
//        };
    }
}
