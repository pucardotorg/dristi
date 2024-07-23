package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CaseConvertClass {

    private CaseConvertClass() {
    }

    public static <E, P> P convertTo(JsonNode jsonNode, Class<E> valueType) throws IOException {
        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);
        if (jsonNode.isArray()) {
            ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
            return reader.readValue(jsonNode);
        } else {
            return (P) objectMapper.treeToValue(jsonNode, valueType);
        }
    }
}