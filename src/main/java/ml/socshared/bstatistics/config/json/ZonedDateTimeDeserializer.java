package ml.socshared.bstatistics.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {
    public ZonedDateTimeDeserializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(p.getLongValue()), ZoneOffset.UTC);
    }
}
