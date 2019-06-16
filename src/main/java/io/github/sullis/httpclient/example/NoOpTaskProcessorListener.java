package io.github.sullis.httpclient.example;

import java.net.URL;

public class NoOpTaskProcessorListener
    implements TaskProcessorListener {
    @Override
    public void statusFetchStarted(URL url) {
        // do nothing
    }
}
