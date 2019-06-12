package io.github.sullis.httpclient.example;

import java.net.http.HttpClient;

public class TaskProcessor {
    private final HttpClient _httpClient;
    private final boolean _failFast;
    private final int _maxConcurrency;

    public TaskProcessor(HttpClient client, boolean failFast, int maxConcurrency) {
        this._httpClient = client;
        this._failFast = failFast;
        this._maxConcurrency = maxConcurrency;
    }

    public void execute() { /* todo */ }

}
