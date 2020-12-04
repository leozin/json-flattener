package com.leozin.utils.json.flattener.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Default wrapper interface for OS-related operations (read/write/exit)
 */
public interface OS {

  void printErr(String message);

  void exit(int statusCode);

  InputStream getStdin();

  PrintStream getStdout();

   boolean isStdinReady() throws IOException;
}
