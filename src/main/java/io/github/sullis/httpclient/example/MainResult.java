package io.github.sullis.httpclient.example;

import com.google.auto.value.AutoValue;
import java.util.Optional;

@AutoValue
abstract class MainResult {
    static MainResult create(ExitCode code, Optional<String> msg) {
        return new AutoValue_MainResult(code, msg);
    }

    abstract public ExitCode exitCode();
    abstract public Optional<String> message();
}
