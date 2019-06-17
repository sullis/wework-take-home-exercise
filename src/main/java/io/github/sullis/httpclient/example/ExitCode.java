package io.github.sullis.httpclient.example;

public enum ExitCode {
    OK(0),  ERROR(1);

    private final int _code;

    ExitCode(int n) {
      _code = n;
    }

    public int code() { return _code; }
}
