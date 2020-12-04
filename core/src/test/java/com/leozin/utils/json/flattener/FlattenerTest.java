package com.leozin.utils.json.flattener;

import static org.junit.jupiter.api.Assertions.*;
import static org.skyscreamer.jsonassert.JSONAssert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FlattenerTest {

  private Flattener flattener;

  @BeforeEach
  public void setup() {
    flattener = Flattener.getInstance();
  }

  @Test
  @DisplayName("Basic JSON conversion")
  public void flatten_simpleJson() throws Exception {
    // given
    String json =
        "{\n"
            + "  \"name\": \"test\",\n"
            + "  \"details\": {\n"
            + "    \"id\": 4\n"
            + "  }\n"
            + "}";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals("{\"name\": \"test\", \"details.id\": 4}", result, true);
  }

  @Test
  @DisplayName("Conversion with empty elements")
  public void flatten_withEmptyElements() throws Exception {
    // given
    String json =
        "{\n"
            + "  \"name\": \"test\",\n"
            + "  \"empty1\": {},\n"
            + "  \"details\": {\n"
            + "    \"id\": 4,\n"
            + "    \"empty2\": {}\n"
            + "  }\n"
            + "}";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals(
        "{\"name\": \"test\", \"empty1\": {}, \"details.id\": 4, \"details.empty2\": {}}",
        result,
        true);
  }

  @Test
  @DisplayName("Conversion with null elements")
  public void flatten_withNullElements() throws Exception {
    // given
    String json =
        "{\"name\": null, \"details\": { \"id\": null } }";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals("{\"name\": null, \"details.id\": null}", result, true);
  }

  @Test
  @DisplayName("JSON with arrays")
  public void flatten_withArrays_throwsException() {
    // given
    String json = "{\"names\": ['abc']\n" + "}";

    // when/then
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    assertThrows(IllegalStateException.class, () ->
        flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output)
    );
  }

  @Test
  @DisplayName("Empty String conversion")
  public void flatten_emptyString_returnsEmptyJson() throws Exception {
    // given
    String json = "";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals("{}", result, true);
  }

  @Test
  @DisplayName("Empty JSON conversion")
  public void flatten_emptyJson_returnsEmptyJson() throws Exception {
    // given
    String json = "{}";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals("{}", result, true);
  }

  @Test
  @DisplayName("JSON conversion where nothing needs to be converted")
  public void flatten_nothingToBeFlattened_returnsSameJson() throws Exception {
    // given
    String json = "{ \"id\": 1, \"name\": \"test\", \"enabled\": false, \"origin\": null, \"scale\": 1.23 }";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals(json, result, true);
  }

  @Test
  @DisplayName("Invalid JSON conversion")
  public void flatten_invalidJson_throwsException() {
    // given
    String json = "boh";

    // when/then
    assertThrows(IllegalStateException.class, () ->
        flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), new ByteArrayOutputStream())
    );
  }

  @Test
  @DisplayName("Skewed JSON conversion ")
  public void flatten_skewedJson_with4levels_returnsFlattenedJson() throws Exception {
    // given
    String json = "{ \"level1\": { \"level2\": { \"level3\": { \"level4\": \"value\" } } } }";

    // when
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    flattener.flattenJson(new ByteArrayInputStream(json.getBytes()), output);

    // then
    String result = new String(output.toByteArray());
    assertEquals("{ \"level1.level2.level3.level4\": \"value\" }", result, true);
  }
}
