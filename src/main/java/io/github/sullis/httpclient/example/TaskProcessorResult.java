package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class TaskProcessorResult {
    static TaskProcessorResult create(long successCount, long failureCount) {
        return new AutoValue_TaskProcessorResult(successCount, failureCount);
    }
    abstract long successCount();
    abstract long failureCount();
}
