package com.leozin.utils.json.flattener.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Default implementation using standard IO/OS operations
 */
public class DefaultOS implements OS {

  @Override
  public void printErr(String message) {
    System.err.println(message);
  }

  @Override
  public void exit(int statusCode) {
    System.exit(statusCode);
  }

  @Override
  public InputStream getStdin() {
    return System.in;
  }

  @Override
  public PrintStream getStdout() {
    return System.out;
  }

  @Override
  public boolean isStdinReady() throws IOException {
    return new InputStreamReader(getStdin()).ready();
  }
}
