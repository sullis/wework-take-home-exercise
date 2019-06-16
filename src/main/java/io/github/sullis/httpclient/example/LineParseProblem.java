package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class LineParseProblem {
    static LineParseProblem create(long lineNumber, String description) {
        return new AutoValue_LineParseProblem(lineNumber, description);
    }
    abstract long lineNumber();
    abstract String description();
}
