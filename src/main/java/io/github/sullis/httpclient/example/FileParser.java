package io.github.sullis.httpclient.example;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.common.collect.ImmutableList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class FileParser {

    public FileParseResult parse(java.io.File file) throws IOException {
        if (!file.exists()) {
            throw new FileParseException("file does not exist");
        }
        FileReader reader = new FileReader(file);
        CSVParser parser = CSVFormat.DEFAULT.parse(reader);
        ImmutableList.Builder urlListBuilder = ImmutableList.builder();
        ImmutableList.Builder problemListBuilder = ImmutableList.builder();
        parser.iterator().forEachRemaining(record -> {
            final long lineNum = parser.getCurrentLineNumber();
            System.out.println(lineNum);
            if (lineNum > 1) {
                String website = record.get(1);
                try {
                    urlListBuilder.add(new URL("https://" + website));
                }
                catch (MalformedURLException ex) {
                    problemListBuilder.add(new LineParseProblem(lineNum, ex.getMessage()));
                }
            }
        });
        return new FileParseResult(urlListBuilder.build(), problemListBuilder.build());
    }

    static class LineParseProblem {
      private final long _lineNumber;
      private final String _description;

      public LineParseProblem(long lineNumber, String description) {
        _lineNumber = lineNumber;
        _description = description;
      }
    }

    static class FileParseResult {
        private ImmutableList<URL> _urls;
        private ImmutableList<LineParseProblem> _problems;

        public FileParseResult(ImmutableList<URL> urls, ImmutableList<LineParseProblem> problems) {
            this._urls = urls;
            this._problems = problems;
        }

        public ImmutableList<LineParseProblem> getProblems() { return _problems; }
        public ImmutableList<URL> getUrls() { return _urls; }
    }

    static class FileParseException extends RuntimeException {
        public FileParseException(String msg) {
            super(msg);
        }

        public FileParseException(String msg, Exception ex) {
            super(msg, ex);
        }
    }
}
