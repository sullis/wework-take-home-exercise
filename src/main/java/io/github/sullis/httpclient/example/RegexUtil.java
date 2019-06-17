package io.github.sullis.httpclient.example;

import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class RegexUtil {
    private RegexUtil() { /* no-op */ }

    static public final Pattern DEFAULT_PATTERN = Pattern.compile("google", CASE_INSENSITIVE);
}
