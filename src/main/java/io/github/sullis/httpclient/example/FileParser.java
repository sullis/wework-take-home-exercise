package io.github.sullis.httpclient.example;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import io.atlassian.fugue.Either;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class FileParser {

    public Iterator<Either<LineParseProblem, URL>> parse(java.io.InputStream input, Charset charset) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, charset);
        CSVParser parser = CSVFormat.DEFAULT.parse(reader);
        Iterator<CSVRecord> iter = parser.iterator();
        iter.next();  // first line is a header
        return new CsvRecordIterator(iter);
    }

    private class CsvRecordIterator
      implements Iterator<Either<LineParseProblem, URL>> {
        private Iterator<CSVRecord> _iter;
        private AtomicLong _lineCount = new AtomicLong(0);

        public CsvRecordIterator(Iterator<CSVRecord> iter) {
            _iter = iter;
        }

        @Override
        public boolean hasNext() {
            return _iter.hasNext();
        }

        @Override
        public Either<LineParseProblem, URL> next() {
            CSVRecord record = _iter.next();
            long lineNum = _lineCount.incrementAndGet();
            String website = record.get(1).trim();
            try {
                return Either.right(new URL("https://" + website));
            } catch (MalformedURLException ex) {
                return Either.left(LineParseProblem.create(lineNum, ex.getMessage()));
            }
        }
    }
}
