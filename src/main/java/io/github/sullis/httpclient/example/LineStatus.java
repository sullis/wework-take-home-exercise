package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class LineSkipped {
    static LineStatus create(long lineNumber, boolean isHeader, String description) {
        return new AutoValue_LineSkipped(lineNumber, isHeader, description);
    }
    abstract long lineNumber();
    abstract String description();
    abstract boolean isHeader();
}
