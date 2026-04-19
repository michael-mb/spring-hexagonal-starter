package com.cozisoft.starter.shared.core;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class StringUtils {

    public static boolean isStringBlank(String toCheck) {
        return toCheck == null || toCheck.isBlank();
    }

    public static String replacePlaceholders(String inputString, List<String> values) {
        int currentIndex = 0;
        String regex = "\\{\\}";
        while (inputString.contains("{}") && values != null && currentIndex < values.size()) {
            inputString = inputString.replaceFirst(regex, values.get(currentIndex++));
        }
        return inputString;
    }

    public static boolean containsIgnoreCase(String value, String searchTerm) {
        if (isStringBlank(value) || isStringBlank(searchTerm)) return false;
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
