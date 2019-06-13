package io.github.sullis.httpclient.example;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.common.collect.ImmutableList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class FileParser {

    public ImmutableList<URL> parse(java.io.File file) throws IOException {
        if (!file.exists()) {
            throw new FileParseException("file does not exist");
        }
        FileReader reader = new FileReader(file);
        CSVParser parser = CSVFormat.DEFAULT.parse(reader);
        ImmutableList.Builder builder = ImmutableList.builder();
        parser.iterator().forEachRemaining(record -> {
            final long lineNum = parser.getCurrentLineNumber();
            System.out.println(lineNum);
            if (lineNum > 1) {
                String website = record.get(1);
                try {
                    builder.add(new URL("https://" + website));
                }
                catch (MalformedURLException ex) {
                    throw new FileParseException("line:" + lineNum, ex);
                }
            }
        });
        return builder.build();
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
