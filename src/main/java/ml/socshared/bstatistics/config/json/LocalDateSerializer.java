package ml.socshared.bstatistics.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.*;

public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.toEpochSecond(LocalTime.MIN, ZoneOffset.UTC));
    }

}
