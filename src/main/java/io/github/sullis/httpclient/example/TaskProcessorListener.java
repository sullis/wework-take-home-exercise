package io.github.sullis.httpclient.example;

import java.net.URL;

public interface TaskProcessorListener {
    void statusFetchStarted(URL url);
}
