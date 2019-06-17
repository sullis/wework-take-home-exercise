package io.github.sullis.httpclient.example;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static io.github.sullis.httpclient.example.RegexUtil.DEFAULT_PATTERN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexUtilTest {
    @Test
    public void patternFind() {
        Pattern p = DEFAULT_PATTERN;
        assertTrue(p.matcher("AAAgoogleBBB").find());
        assertTrue(p.matcher("google").find());
        assertTrue(p.matcher("Google").find());
        assertTrue(p.matcher("google.com").find());
        assertFalse(p.matcher("aaa").find());
    }
}
