//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.pucar.dristi.util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;


import java.io.IOException;
import java.util.List;


public class CaseConvertClass {
    public CaseConvertClass() {
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
