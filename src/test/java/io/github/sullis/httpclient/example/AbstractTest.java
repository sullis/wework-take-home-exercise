package io.github.sullis.httpclient.example;

import java.util.Properties;

public class AbstractTest {
    static {
        initLogging();
    }

    static void initLogging() {
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.debug", "true");
        props.setProperty("jdk.httpclient.HttpClient.log", "all");
        props.setProperty("org.slf4j.simpleLogger.log.jdk.httpclient.HttpClient", "debug");
    }
}
