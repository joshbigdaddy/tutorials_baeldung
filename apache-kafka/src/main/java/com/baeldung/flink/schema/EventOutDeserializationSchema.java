package com.baeldung.flink.schema;

import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.model.InputMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class EventOutDeserializationSchema implements DeserializationSchema<EventOut> {

    static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public EventOut deserialize(byte[] bytes) throws IOException {

        return objectMapper.readValue(bytes, EventOut.class);
    }

    @Override
    public boolean isEndOfStream(EventOut inputMessage) {
        return false;
    }

    @Override
    public TypeInformation<EventOut> getProducedType() {
        return TypeInformation.of(EventOut.class);
    }
}
