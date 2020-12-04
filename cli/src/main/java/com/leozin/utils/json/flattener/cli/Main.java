package com.leozin.utils.json.flattener.cli;

import com.leozin.utils.json.flattener.Flattener;
import java.io.IOException;

public class Main {

  public static final int NO_INPUT_EXIT_CODE = 1;
  public static final int EXCEPTION_EXIT_CODE = 2;

  public static final String DEFAULT_EMPTY_INPUT_ERROR_MSG = "Error: No input received. Please pipe something into it, Eg.: cat file.json | jf";
  public static final String DEFAULT_INPUT_STREAM_CHECK_ERROR_MSG = "Error acquiring input stream: ";
  public static final String DEFAULT_FLATTENER_ERROR_MSG = "Error: ";

  public static OS os = new DefaultOS();

  public static void main(String[] args) {
    checkInteractiveMode();
  }

  private static void checkInteractiveMode() {
    try {
      if(!os.isStdinReady()) {
        printErrorAndExit(
            DEFAULT_EMPTY_INPUT_ERROR_MSG,
            NO_INPUT_EXIT_CODE);
      } else {
        executeJsonFlattener();
      }
    } catch (IOException ex) {
      printErrorAndExit(DEFAULT_INPUT_STREAM_CHECK_ERROR_MSG + ex.getMessage(), EXCEPTION_EXIT_CODE);
    }
  }

  private static void executeJsonFlattener() {
    try {
      Flattener.getInstance().flattenJson(os.getStdin(), os.getStdout());
    } catch(Exception ex) {
      printErrorAndExit(DEFAULT_FLATTENER_ERROR_MSG + ex.getMessage(), EXCEPTION_EXIT_CODE);
    }
  }

  private static void printErrorAndExit(String message, int exitCode) {
    os.printErr(message);
    os.exit(exitCode);
  }
}
