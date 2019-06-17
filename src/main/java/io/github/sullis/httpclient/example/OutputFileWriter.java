package io.github.sullis.httpclient.example;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class OutputFileWriter implements Closeable {
    static public final String DEFAULT_FILENAME = "results.txt";

    private final FileWriter _writer;

    public OutputFileWriter(String filename) throws IOException {
      _writer = new FileWriter(filename, CharSetUtil.DEFAULT_CHARSET, false);
    }

    public synchronized void writeLine(URL u) throws IOException {
        _writer.write("\"" + u + "\"\n");
        _writer.flush();
    }

    public synchronized void close() throws IOException {
        if (_writer != null) {
            _writer.flush();
            _writer.close();
        }
    }

}
