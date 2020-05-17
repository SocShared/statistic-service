package ml.socshared.bstatistics.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    public LocalDateTimeDeserializer() {
        super(java.time.LocalDateTime.class);
    }

    @Override
    public java.time.LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return java.time.LocalDateTime.ofEpochSecond(p.getLongValue(), 0, ZoneOffset.UTC);
    }
}
