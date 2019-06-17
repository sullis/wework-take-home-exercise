package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class IgnoredLine {
    static IgnoredLine create(long lineNumber, boolean isHeader, String description) {
        return new AutoValue_IgnoredLine(lineNumber, isHeader, description);
    }
    abstract long lineNumber();
    abstract boolean isHeader();
    abstract String description();
}
