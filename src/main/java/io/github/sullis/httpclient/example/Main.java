
package io.github.sullis.httpclient.example;

import io.atlassian.fugue.Either;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) {

    try {
      CommandLine cmdLine = CommandLineUtil.build(args);
      System.out.println("args: " + cmdLine.getArgList());
      final boolean verbose = cmdLine.hasOption("verbose");
      File f = new File("input.txt"); // FIXME
      FileInputStream input = new FileInputStream(f);
      FileParser parser = new FileParser();
      Stream<Either<LineStatus, URL>> stream = parser.parse(input, CharSetUtil.DEFAULT_CHARSET);
      Stream<URL> urlStream = stream.filter(item -> item.isRight()).map(item -> item.right().get());
      TaskProcessor p = new TaskProcessor(HttpClientUtil.build(),
              getListener(verbose),
              urlStream,
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
