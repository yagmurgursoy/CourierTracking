package org.example.couriertrackingapp.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.couriertrackingapp.domain.dtos.CourierLocationRequest;
import org.example.couriertrackingapp.domain.entities.CourierLocation;
import org.example.couriertrackingapp.domain.entities.Store;
import org.example.couriertrackingapp.factories.StoreProviderFactory;
import org.example.couriertrackingapp.mappers.CourierMapper;
import org.example.couriertrackingapp.strategies.DistanceStrategy;
import org.example.couriertrackingapp.strategies.HaversineDistanceStrategy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CourierTrackingService {
    private List<Store> stores;
    private final List<CourierLocation> courierLocations = Collections.synchronizedList(new ArrayList<>());
    private final CourierMapper courierMapper;
    private final ConcurrentHashMap<UUID, LocalDateTime> recentEntriesInRadius = new ConcurrentHashMap<>();
    private final DistanceStrategy distanceStrategy = new HaversineDistanceStrategy();
    private final StoreProviderFactory storeProviderFactory;

    @PostConstruct
    public void initStores() {
        this.stores = storeProviderFactory.getProvider("fileStoreProvider").getStores();
    }

    public Double getTotalTravelDistance(UUID courierId) {
        if (courierLocations.size() < 2) {
            return (double) 0;
        }

        List<CourierLocation> sortedLocations = courierLocations.stream()
                .filter(location -> location.getCourierId().equals(courierId))
                .sorted(Comparator.comparing(CourierLocation::getTimestamp))
                .toList();

        return IntStream.range(0, sortedLocations.size() - 1)
                .mapToDouble(i -> distanceStrategy.calculate(
                        sortedLocations.get(i).getLatitude(),
                        sortedLocations.get(i).getLongitude(),
                        sortedLocations.get(i + 1).getLatitude(),
                        sortedLocations.get(i + 1).getLongitude()
                ))
                .sum();
    }

    public void saveCourierLocation(CourierLocationRequest request){
        CourierLocation location = courierMapper.convertToEntity(request);
        location.setTimestamp(LocalDateTime.now());
        courierLocations.add(location);

        checkProximityToStores(location);
    }

    private void checkProximityToStores(CourierLocation courierLocation) {
        for (Store store : Objects.requireNonNull(stores)) {
            if (isWithinRadius(
                    courierLocation.getLatitude(),
                    courierLocation.getLongitude(),
                    store.getLat(),
                    store.getLng()
            )) {
                /// flag de olabilir ya da ayrı bir liste
                LocalDateTime lastEntryTime = recentEntriesInRadius.get(courierLocation.getCourierId());
                if (lastEntryTime != null && Duration.between(lastEntryTime, LocalDateTime.now()).toMinutes() < 1) {
                    return;
                }

                recentEntriesInRadius.put(courierLocation.getCourierId(), LocalDateTime.now());
                logStoreEntry(courierLocation, store);
            }
        }
    }

    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2) {
        double distance = distanceStrategy.calculate(lat1, lon1, lat2, lon2);
        return distance <= 100;
    }

    public void logStoreEntry(CourierLocation courierLocation, Store store) {
        System.out.println("Courier " + courierLocation.getCourierId() +
                " entered store " + store.getName() + " at " + courierLocation.getTimestamp()
                );
    }


}
