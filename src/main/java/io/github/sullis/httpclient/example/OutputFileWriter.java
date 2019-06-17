package io.github.sullis.httpclient.example;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFileWriter implements Closeable {
    static private final String DEFAULT_FILENAME = "results.txt";

    private final FileWriter _writer;

    public OutputFileWriter() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public OutputFileWriter(String filename) throws IOException {
      _writer = new FileWriter(filename, CharSetUtil.DEFAULT_CHARSET, false);
    }

    public synchronized void writeLine(String content) throws IOException {
        _writer.write(content);
        _writer.write("\n");
        _writer.flush();
    }

    public synchronized void close() throws IOException {
        if (_writer != null) {
            _writer.close();
        }
    }

}
