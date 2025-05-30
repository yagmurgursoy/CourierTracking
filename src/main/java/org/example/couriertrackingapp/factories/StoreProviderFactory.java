package org.example.couriertrackingapp.factories;


import lombok.RequiredArgsConstructor;
import org.example.couriertrackingapp.factories.providers.StoreProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StoreProviderFactory {

    private final Map<String, StoreProvider> providers;

    public StoreProvider getProvider(String sourceType) {
        return providers.getOrDefault(sourceType, providers.get("fileStoreProvider"));
    }

}
