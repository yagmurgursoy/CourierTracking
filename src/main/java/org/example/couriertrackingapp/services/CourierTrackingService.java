package org.example.couriertrackingapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.example.couriertrackingapp.domain.entities.CourierLocation;
import org.example.couriertrackingapp.domain.entities.Store;
import org.example.couriertrackingapp.domain.dtos.CourierLocationRequest;
import org.example.couriertrackingapp.mappers.CourierMapper;
import org.example.couriertrackingapp.mappers.StoreMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.example.couriertrackingapp.utils.FileUtils.readListFromJson;

@Service
@RequiredArgsConstructor
public class CourierTrackingService {
    private final List<Store> stores = getStores();
    private final List<CourierLocation> courierLocations = Collections.synchronizedList(new ArrayList<>());
    private final CourierMapper courierMapper;
    private final StoreMapper storeMapper;
    private final ConcurrentHashMap<UUID, LocalDateTime> recentEntries = new ConcurrentHashMap<>();


    public Double getTotalTravelDistance(UUID courierId) {
        if (courierLocations.size() < 2  ||  courierLocations == null) {
            return (double) 0;
        }

        List<CourierLocation> sorted = courierLocations.stream()
                .sorted(Comparator.comparing(CourierLocation::getTimestamp))
                .toList();

        return IntStream.range(0, sorted.size() - 1)
                .mapToDouble(i -> haversine(
                        sorted.get(i).getLatitude(),
                        sorted.get(i).getLongitude(),
                        sorted.get(i + 1).getLatitude(),
                        sorted.get(i + 1).getLongitude()
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
        for (Store store : stores) {
            if (isWithinRadius(
                    courierLocation.getLatitude(),
                    courierLocation.getLongitude(),
                    store.getLat(),
                    store.getLng(),
                    100)) {

                LocalDateTime lastEntryTime = recentEntries.get(courierLocation.getCourierId());
                if (lastEntryTime != null && Duration.between(lastEntryTime, LocalDateTime.now()).toMinutes() < 1) {
                    return;
                }

                recentEntries.put(courierLocation.getCourierId(), LocalDateTime.now());
                logStoreEntry(courierLocation, store);
            }
        }
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public void logStoreEntry(CourierLocation courierLocation, Store store) {
        System.out.println("Courier " + courierLocation.getCourierId() +
                " entered store " + store.getName() + " at " + LocalDateTime.now() //todo check
                );
    }

    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, int radiusInMeters) {
        double distance = haversine(lat1, lon1, lat2, lon2);
        return distance <= radiusInMeters;
    }

    private List<Store> getStores() {
        try {
            File file = new ClassPathResource("files/stores.json").getFile();
            return readListFromJson(file,new TypeReference<List<Store>>() {});
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
