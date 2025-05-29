package org.example.couriertrackingapp.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> readListFromJson(File file, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(file, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
