package com.debrains.debrainsApi.hateoas;

import com.debrains.debrainsApi.entity.Til;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TilSerializer extends JsonSerializer<Til> {
    @Override
    public void serialize(Til til, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", til.getId());
        jsonGenerator.writeEndObject();
    }
}
