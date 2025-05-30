package org.example.couriertrackingapp.factories;

import org.example.couriertrackingapp.factories.providers.StoreProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreProviderFactoryTest {

    private StoreProviderFactory factory;
    private StoreProvider mockDbProvider;
    private StoreProvider mockFileProvider;

    @BeforeEach
    void setUp() {
        mockDbProvider = Mockito.mock(StoreProvider.class);
        mockFileProvider = Mockito.mock(StoreProvider.class);

        Map<String, StoreProvider> providerMap = new HashMap<>();
        providerMap.put("dbStoreProvider", mockDbProvider);
        providerMap.put("fileStoreProvider", mockFileProvider);

        factory = new StoreProviderFactory(providerMap);
    }

    @Test
    void shouldReturnSpecificProviderWhenKeyExists() {
        StoreProvider result = factory.getProvider("dbStoreProvider");
        assertEquals(mockDbProvider, result);
    }

    @Test
    void shouldReturnDefaultProviderWhenKeyDoesNotExist() {
        StoreProvider result = factory.getProvider("nonExistentProvider");
        assertEquals(mockFileProvider, result);
    }
}
