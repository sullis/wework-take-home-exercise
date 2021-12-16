# java-httpclient-example


# System requirements

This project requires:
- JDK 11 or higher
- Maven 3.6.3

# Java 11 development kit

- [Amazon Corretto 11.x](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)

# Build and run

```

./build.sh  && ./run.sh

```

# Core classes
- [Main.java](https://github.com/sullis/java-httpclient-example/blob/main/src/main/java/io/github/sullis/httpclient/example/Main.java)
- [FileParser.java](https://github.com/sullis/java-httpclient-example/blob/main/src/main/java/io/github/sullis/httpclient/example/FileParser.java)
- [UrlProcessor.java](https://github.com/sullis/java-httpclient-example/blob/main/src/main/java/io/github/sullis/httpclient/example/UrlProcessor.java)
- [OutputFileWriter.java](https://github.com/sullis/java-httpclient-example/blob/main/src/main/java/io/github/sullis/httpclient/example/OutputFileWriter.java)

# Specification

- [Searcher](https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html)

# HTTP client implementation

This project uses the Java platform HttpClient:

[java.net.http.HttpClient](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html)

This HttpClient is available in Java 11 and above.

# Third party libraries

- [Apache commons-cli](https://commons.apache.org/proper/commons-cli/)
- [Apache commons-csv](https://commons.apache.org/proper/commons-csv/)
- [Google Guava](https://github.com/google/guava)
- [Google AutoValue](https://github.com/google/auto/blob/master/value/userguide/index.md)
- [Atlassian Fugue](https://bitbucket.org/atlassian/fugue/src/master/readme.md)
- [slf4j](https://www.slf4j.org/)
- [logback](https://logback.qos.ch/)
- [JUnit 5.x](https://junit.org/junit5/)

# Continuous integration

This project uses [GitHub Actions](https://github.com/sullis/java-httpclient-example/actions)
