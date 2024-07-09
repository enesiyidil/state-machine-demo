package com.enes.constant;

public class Regex {

    private Regex() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PATTERN_FIND_STRING_BETWEEN_AT = "@(\\w+)@";

    public static final String PATTERN_FIND_DOLLAR_AMOUNT = "\\$(\\d+(\\.\\d+)?)\\$";
}
