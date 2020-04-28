package com.springboot.practice.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.utils.json.supplier.JsonDataSupplier;

import java.io.IOException;
import java.io.InputStream;

public class JsonParser {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T buildObjectFromJSON(JsonDataSupplier dataSupplier, TypeReference<T> typeReference) {
        T objectToReturn = null;
        InputStream inputStream = JsonParser.class.getClassLoader().getResourceAsStream(dataSupplier.getFilePath());

        try {
            assert inputStream != null;
            objectToReturn = objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert objectToReturn != null;
        return objectToReturn;
    }

    public static String readJSON(JsonDataSupplier dataSupplier) {
        String parsedJSON = null;
        InputStream inputStream = JsonParser.class.getClassLoader().getResourceAsStream(dataSupplier.getFilePath());

        try {
            assert inputStream != null;
            JsonNode parser = objectMapper.readTree(inputStream);
            parsedJSON = parser.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert parsedJSON != null;
        return parsedJSON;
    }
}
