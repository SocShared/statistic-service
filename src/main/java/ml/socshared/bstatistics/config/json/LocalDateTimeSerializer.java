package ml.socshared.bstatistics.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    public  LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }


    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.toEpochSecond(ZoneOffset.UTC));
    }
}
