import com.leozin.utils.json.flattener.cli.OS;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * OS stub class for validating the interaction between the main method and System libraries
 */
public class OSStub implements OS {

  public int exitCode;
  public String stderr;
  public boolean ready;
  public InputStream stdin;
  public PrintStream stdout;

  @Override
  public void exit(int exitCode) {
    this.exitCode = exitCode;
  }

  @Override
  public void printErr(String message) {
    stderr = message;
  }

  @Override
  public boolean isStdinReady() throws IOException {
    return new InputStreamReader(getStdin()).ready();
  }

  @Override
  public InputStream getStdin() {
    return stdin;
  }

  @Override
  public PrintStream getStdout() {
    return stdout;
  }
}
