package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class LineStatus {
    static LineStatus create(long lineNumber, boolean isHeader, String description) {
        return new AutoValue_LineStatus(lineNumber, isHeader, description);
    }
    abstract long lineNumber();
    abstract boolean isHeader();
    abstract String description();
}
