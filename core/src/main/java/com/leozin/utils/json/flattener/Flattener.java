package com.leozin.utils.json.flattener;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Flattener {

  public static final String FLATTENED_FIELD_SEPARATOR = ".";
  public static final String LINE_SEPARATOR_VARIABLE = "line.separator";
  private final JsonFactory factory;

  private static Flattener instance;

  private Flattener() {
    factory = new JsonFactory();
  }

  public static Flattener getInstance() {
    if (instance == null) {
      instance = new Flattener();
    }
    return instance;
  }

  public void flattenJson(InputStream input, OutputStream output) {
    try {
      JsonGenerator generator = buildGenerator(output);

      parseJson(input, generator);
    } catch (Exception e) {
      throw new IllegalStateException("JSON could not be flattened. Reason: " + e.getMessage(), e);
    }
  }

  private JsonGenerator buildGenerator(OutputStream output) throws IOException {
    JsonGenerator generator = factory.createGenerator(output, JsonEncoding.UTF8);
    generator.useDefaultPrettyPrinter();
    return generator;
  }

  private void parseJson(InputStream json, JsonGenerator generator) throws IOException {
    try {
      generator.writeStartObject();
      JsonParser parser = factory.createParser(json);
      parser.nextToken();
      appendFieldName("", parser, generator);
    } finally {
      generator.writeEndObject();
      generator.writeRaw(System.getProperty(LINE_SEPARATOR_VARIABLE));
      generator.close();
    }
  }

  private void appendFieldName(String prefix, JsonParser parser,
      JsonGenerator generator) throws IOException {
    JsonToken token = parser.nextToken();
    if (isObjectFieldEmpty(prefix, generator, token)) {
      return;
    }
    while (token != JsonToken.END_OBJECT && token != null) {
      String fieldName = buildFieldName(prefix, parser.getCurrentName());
      token = parser.nextToken();
      if (token == JsonToken.START_OBJECT) {
        appendFieldName(fieldName, parser, generator);
      } else {
        writeField(parser, generator, token, fieldName);
      }
      token = parser.nextToken();
    }
  }

  private String buildFieldName(String prefix, String name) throws IOException {
    if ("".equals(prefix)) {
      return name;
    }
    return prefix + FLATTENED_FIELD_SEPARATOR + name;
  }

  private boolean isObjectFieldEmpty(String prefix, JsonGenerator generator, JsonToken token)
      throws IOException {
    if (token == JsonToken.END_OBJECT) {
      if (!"".equals(prefix)) {
        generator.writeObjectFieldStart(prefix);
        generator.writeEndObject();
      }
      return true;
    }
    return false;
  }

  private void writeField(JsonParser parser, JsonGenerator generator, JsonToken token,
      String fieldName) throws IOException {
    switch (token) {
      case VALUE_NULL:
        generator.writeNullField(fieldName);
        break;
      case VALUE_STRING:
        generator.writeStringField(fieldName, parser.getValueAsString());
        break;
      case VALUE_FALSE:
      case VALUE_TRUE:
        generator.writeBooleanField(fieldName, parser.getValueAsBoolean());
        break;
      case VALUE_NUMBER_INT:
      case VALUE_NUMBER_FLOAT:
        generator.writeNumberField(fieldName, parser.getDecimalValue());
        break;
      default:
        throw new UnsupportedOperationException("Arrays are not allowed");
    }
  }
}
