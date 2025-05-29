package org.example.couriertrackingapp;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.couriertrackingapp.domain.entities.Store;
import org.example.couriertrackingapp.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void testReadListFromJson_validFile() {
        File validFile = new File("src/main/resources/files/stores.json");

        List<Store> stores = FileUtils.readListFromJson(validFile, new TypeReference<List<Store>>() {});

        assertNotNull(stores, "The returned list should not be null for a valid file.");
        assertFalse(stores.isEmpty(), "The list should not be empty for a valid file.");
        assertEquals(5, stores.size(), "The list size should match the number of entries in the file.");
    }

    @Test
    void testReadListFromJson_invalidJsonFile() {
        File invalidFile = new File("src/test/resources/files/stores_invalid.json");
        TypeReference<List<Store>> typeReference = new TypeReference<>() {};

        List<Store> stores = FileUtils.readListFromJson(invalidFile, typeReference);

        assertNull(stores, "The returned list should be null when the JSON file is invalid.");
    }

    @Test
    void testReadListFromJson_fileNotFound() {
        File nonExistentFile = new File("src/test/resources/files/non_existent.json");
        TypeReference<List<Store>> typeReference = new TypeReference<>() {};

        List<Store> stores = FileUtils.readListFromJson(nonExistentFile, typeReference);

        assertNull(stores, "The returned list should be null when the file does not exist.");
    }

    @Test
    void testReadListFromJson_emptyFile() {
        File emptyFile = new File("src/test/resources/files/stores_empty.json");
        TypeReference<List<Store>> typeReference = new TypeReference<>() {};

        List<Store> stores = FileUtils.readListFromJson(emptyFile, typeReference);

        assertNull(stores, "The returned list should be null for an empty JSON file.");
    }

    @Test
    void testReadListFromJson_nullFile() {
        File nullFile = null;
        TypeReference<List<Store>> typeReference = new TypeReference<>() {};

        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.readListFromJson(nullFile, typeReference),
                "An IllegalArgumentException should be thrown for a null file.");
    }

    @Test
    void testReadListFromJson_incorrectTypeReference() {
        File validFile = new File("src/test/resources/files/stores_valid.json");
        TypeReference<List<String>> incorrectTypeRef = new TypeReference<>() {};

        List<String> result = FileUtils.readListFromJson(validFile, incorrectTypeRef);

        assertNull(result, "The returned list should be null if the TypeReference does not match the file content.");
    }
}