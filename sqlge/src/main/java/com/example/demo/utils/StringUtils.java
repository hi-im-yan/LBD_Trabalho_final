package com.example.demo.utils;

public abstract class StringUtils {

    public static boolean containWords(String inputString, String[] words) {
        for (String word : words) {
            if (inputString.contains(word)) {
                return true;
            }
        }

        return false;
    }
}
