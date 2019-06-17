package io.github.sullis.httpclient.example;

import java.util.Properties;

public class AbstractTest {
    static {
        initLogging();
    }

    static void initLogging() {
        Properties props = System.getProperties();

        props.setProperty("jdk.internal.httpclient.debug", "false");

        // see: https://stackoverflow.com/questions/53215038/how-to-log-request-response-using-java-net-http-httpclient/53231046#53231046
        props.setProperty("jdk.httpclient.HttpClient.log", "requests");
    }
}
