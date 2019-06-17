package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class UrlProcessorResult {
    static UrlProcessorResult create(long processedCount, long failureCount) {
        return new AutoValue_UrlProcessorResult(processedCount, failureCount);
    }
    abstract long processedCount();
    abstract long failureCount();
}
