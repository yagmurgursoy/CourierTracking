package org.example.couriertrackingapp.factories.providers;

import org.example.couriertrackingapp.domain.entities.Store;

import java.util.List;

public interface StoreProvider {
    List<Store> getStores();
}
