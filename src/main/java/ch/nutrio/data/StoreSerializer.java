package ch.nutrio.data;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StoreSerializer extends JsonSerializer<Store> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void serialize(Store value,
                        JsonGenerator gen,
                        SerializerProvider serializers) throws IOException {
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, value);
    gen.writeFieldName(writer.toString());
  }
}
