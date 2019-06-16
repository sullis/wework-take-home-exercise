package io.github.sullis.httpclient.example;

import java.util.Properties;

public class AbstractTest {
    static {
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.debug", "true");
        props.setProperty("org.slf4j.simpleLogger.log.java.net.http", "debug");
    }
}
