
package io.github.sullis.httpclient.example;

import com.google.common.base.Throwables;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class Main {
  public static void main(String[] args) {
    Main main = new Main();
    try {
      Pair<ExitCode, Optional<String>> result = main.execute(args);
      result.right().ifPresent(errMsg -> System.err.println(errMsg));
      System.exit(result.left().code());
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(ExitCode.ERROR.code());
    }
  }

  public Pair<ExitCode, Optional<String>> execute(String[] args) {
    try {
      CommandLine cmdLine = CommandLineUtil.build(args);
      final boolean verbose = cmdLine.hasOption("verbose");
      final String inputFilename = cmdLine.getOptionValue("input");
      File f = new File(inputFilename);
      if (!f.exists()) {
        return Pair.pair(ExitCode.ERROR, Optional.of(f.toString() + " does not exist."));
      }
      if (f.isDirectory()) {
        return Pair.pair(ExitCode.ERROR, Optional.of(f.toString() + " is a directory."));
      }
      if (!f.canRead()) {
        return Pair.pair(ExitCode.ERROR, Optional.of("Unable to read file: " + f.toString()));
      }
      if (f.length() == 0) {
        return Pair.pair(ExitCode.ERROR, Optional.of("Empty file: " + f.toString()));
      }
      try (FileInputStream input = new FileInputStream(f)) {
        FileParser parser = new FileParser();
        Stream<Either<IgnoredLine, URL>> stream = parser.parse(input, CharSetUtil.DEFAULT_CHARSET);
        Stream<URL> urlStream = stream.filter(item -> item.isRight()).map(item -> item.right().get());
        UrlProcessor p = new UrlProcessor(HttpClientUtil.build(),
                getListener(verbose),
                urlStream,
                20);
        CompletableFuture<UrlProcessorResult> future = p.execute();
        UrlProcessorResult processorResult = future.get();
        return Pair.pair(ExitCode.OK, Optional.empty());
      }
    } catch (ParseException ex) {
      System.err.println();
      return Pair.pair(ExitCode.ERROR, Optional.of("Command line parse error:  " + ex.getMessage()));
    } catch (Exception ex) {
      return Pair.pair(ExitCode.ERROR, Optional.of(Throwables.getStackTraceAsString(ex)));
    }
  }

  private static Optional<UrlProcessorListener> getListener(boolean verbose) {
    if (verbose) {
      return Optional.of(new UrlProcessorListenerImpl(new OutputStreamWriter(System.out)));
    }
    else {
      return Optional.empty();
    }

  }

}
