package io.github.sullis.httpclient.example;


import org.apache.commons.cli.*;

public class CommandLineUtil {

    static public CommandLine build(String[] args) throws ParseException {
        Options options;
        options = new Options();
        options.addOption("verbose", "verbose", false, "verbose");
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
