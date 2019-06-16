package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class TaskProcessorResult {
    static TaskProcessorResult create(String description) {
        return new AutoValue_TaskProcessorResult(description);
    }
    abstract String description();
}
