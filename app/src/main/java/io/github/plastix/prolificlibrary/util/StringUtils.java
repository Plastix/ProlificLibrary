package io.github.plastix.prolificlibrary.util;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.equals("");
    }
}
