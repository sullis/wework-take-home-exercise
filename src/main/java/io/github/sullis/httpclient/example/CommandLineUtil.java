package io.github.sullis.httpclient.example;


import org.apache.commons.cli.*;

public class CommandLineUtil {

    static public CommandLine build(String[] args) throws ParseException {
        Options options;
        options = new Options();
        options.addOption("v", "verbose", false, "verbose");
        options.addOption("i", "input", true, "input");
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
