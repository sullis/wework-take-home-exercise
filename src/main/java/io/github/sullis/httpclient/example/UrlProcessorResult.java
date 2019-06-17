package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class UrlProcessorResult {
    static UrlProcessorResult create(long successCount, long failureCount) {
        return new AutoValue_UrlProcessorResult(successCount, failureCount);
    }
    abstract long successCount();
    abstract long failureCount();
}
