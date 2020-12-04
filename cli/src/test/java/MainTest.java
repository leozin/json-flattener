import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leozin.utils.json.flattener.cli.Main;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MainTest {

  private OSStub osStub;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  public void setup() {
    osStub = new OSStub();
    outputStream =  new ByteArrayOutputStream();
    osStub.stdout = new PrintStream(outputStream);

    Main.os = osStub;
  }

  @Test
  @DisplayName("Execute CLI with basic input")
  public void cli_executingWithContent_returnExitCode0() throws IOException {
    // given
    ByteArrayInputStream inputStream = new ByteArrayInputStream("{ }".getBytes());
    osStub.stdin = inputStream;
    osStub.stdout = new PrintStream(outputStream);

    // when
    Main.main(null);

    // then
    assertEquals("{ }\n", new String(outputStream.toByteArray()));
    assertEquals(0, osStub.exitCode);
  }

  @Test
  @DisplayName("Execute CLI without any input")
  public void cli_executingWithoutContent_returnExitCode1() throws IOException {
    // given
    ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
    osStub.stdin = inputStream;

    // when
    Main.main(null);

    // then
    assertEquals("Error: No input received. Please pipe something into it, Eg.: cat file.json | jf",
        osStub.stderr);
    assertEquals(1, osStub.exitCode);
  }

  @Test
  @DisplayName("Execute CLI with an invalid input")
  public void cli_callingWithoutContent_returnErrorCode1() {
    // given
    ByteArrayInputStream inputStream = new ByteArrayInputStream("boh".getBytes());
    osStub.stdin = inputStream;

    // when
    Main.main(null);

    // then
    assertTrue(osStub.stderr.contains("Unrecognized token 'boh'"));
    assertEquals(2, osStub.exitCode);
  }
}
