package io.github.sullis.httpclient.example;

import java.io.IOException;
import java.net.URL;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

public class FileParser {

    public ImmutableList<URL> parse(java.io.File file) throws IOException {
        CharSource source = Files.asCharSource(file, CharSetUtil.DEFAULT_CHARSET);
        // the first line is header information. We don't need it.
        source.readFirstLine();
        // TODO : source.forEachLine();
        return ImmutableList.of(); // FIXME
    }

}
