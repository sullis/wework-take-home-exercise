
package io.github.sullis.httpclient.example;

import com.google.common.base.Throwables;
import io.atlassian.fugue.Either;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.github.sullis.httpclient.example.RegexUtil.DEFAULT_PATTERN;

public class Main {
  public static void main(String[] args) {
    Main main = new Main();
    try {
      MainResult result = main.execute(args);
      PrintStream printStream = (result.exitCode() == ExitCode.OK) ? System.out : System.err;
      result.message().ifPresent(msg -> printStream.println(msg));
      System.exit(result.exitCode().code());
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(ExitCode.ERROR.code());
    }
  }

  public MainResult execute(String[] args) {
    try {
      CommandLine cmdLine = CommandLineUtil.build(args);
      final boolean verbose = cmdLine.hasOption("verbose");
      final String inputFilename = cmdLine.getOptionValue("input");
      File f = new File(inputFilename);
      if (!f.exists()) {
        return MainResult.create(ExitCode.ERROR, Optional.of(f.toString() + " does not exist."));
      }
      if (f.isDirectory()) {
        return MainResult.create(ExitCode.ERROR, Optional.of(f.toString() + " is a directory."));
      }
      if (!f.canRead()) {
        return MainResult.create(ExitCode.ERROR, Optional.of("Unable to read file: " + f.toString()));
      }
      if (f.length() == 0) {
        return MainResult.create(ExitCode.ERROR, Optional.of("Empty file: " + f.toString()));
      }
      try (FileInputStream input = new FileInputStream(f)) {
        FileParser parser = new FileParser();
        OutputFileWriter outputFileWriter = new OutputFileWriter(OutputFileWriter.DEFAULT_FILENAME);
        Stream<Either<IgnoredLine, URL>> stream = parser.parse(input, CharSetUtil.DEFAULT_CHARSET);
        Stream<URL> urlStream = stream.filter(item -> item.isRight()).map(item -> item.right().get());
        Pattern regexPattern = Pattern.compile("google");
        UrlProcessor p = new UrlProcessor(HttpClientUtil.build(),
                getListener(verbose),
                urlStream,
                20,
                new ResponseBodyProcessorImpl(outputFileWriter, DEFAULT_PATTERN));
        CompletableFuture<UrlProcessorResult> future = p.execute();
        UrlProcessorResult processorResult = future.get();
        if (processorResult.processedCount() > 0) {
          return MainResult.create(ExitCode.OK, Optional.empty());
        }
        else {
          return MainResult.create(ExitCode.ERROR, Optional.of("Processed count: " + processorResult.processedCount()));
        }
      }
    } catch (ParseException ex) {
      System.err.println();
      return MainResult.create(ExitCode.ERROR, Optional.of("Command line parse error:  " + ex.getMessage()));
    } catch (Exception ex) {
      return MainResult.create(ExitCode.ERROR, Optional.of(Throwables.getStackTraceAsString(ex)));
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
