
package io.github.sullis.httpclient.example;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class Main {

  public static void main(String[] args) {

    try {
      CommandLine cmdLine = CommandLineUtil.build(args);
      System.out.println("args: " + cmdLine.getArgList());
      TaskProcessor p = new TaskProcessor(HttpClientUtil.build(), true, 20);
      p.execute();
    } catch (ParseException ex) {
      System.err.println("Command line error:  " + ex.getMessage());
      System.exit(1);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
