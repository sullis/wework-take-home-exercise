
package io.github.sullis.httpclient.example;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.OutputStreamWriter;
import java.util.Optional;

public class Main {

  public static void main(String[] args) {

    try {
      CommandLine cmdLine = CommandLineUtil.build(args);
      System.out.println("args: " + cmdLine.getArgList());
      final boolean verbose = cmdLine.hasOption("verbose");
      TaskProcessor p = new TaskProcessor(HttpClientUtil.build(),
              getListener(verbose),
              20);
      p.execute();
    } catch (ParseException ex) {
      System.err.println("Command line error:  " + ex.getMessage());
      System.exit(1);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  private static Optional<TaskProcessorListener> getListener(boolean verbose) {
    if (verbose) {
      return Optional.of(new TaskProcessorListenerImpl(new OutputStreamWriter(System.out)));
    }
    else {
      return Optional.empty();
    }

  }

}
