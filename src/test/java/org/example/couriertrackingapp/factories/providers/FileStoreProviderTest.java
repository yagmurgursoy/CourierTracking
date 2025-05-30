package org.example.couriertrackingapp.factories.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.couriertrackingapp.domain.entities.Store;
import org.example.couriertrackingapp.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileStoreProviderTest {

    @Test
    void shouldReturnListOfStoresWhenJsonIsValid() throws Exception {
        FileStoreProvider provider = new FileStoreProvider();
        File file = new ClassPathResource("files/stores.json").getFile();

        Store dummyStore = new Store();
        List<Store> expectedStores = List.of(dummyStore);

        try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
            utilities.when(() -> FileUtils.readListFromJson(eq(file), any(TypeReference.class)))
                    .thenReturn(expectedStores);

            List<Store> result = provider.getStores();

            assertEquals(expectedStores, result);
        }
    }

    @Test
    void shouldReturnEmptyListOnException() {
        FileStoreProvider provider = new FileStoreProvider();

        try (MockedStatic<FileUtils> utilities = mockStatic(FileUtils.class)) {
            utilities.when(() -> FileUtils.readListFromJson(any(File.class), any(TypeReference.class)))
                    .thenThrow(new RuntimeException("error"));

            List<Store> result = provider.getStores();

            assertEquals(List.of(), result);
        }
    }
}
