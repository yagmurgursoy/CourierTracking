package org.example.couriertrackingapp;

import org.example.couriertrackingapp.domain.dtos.CourierLocationRequest;
import org.example.couriertrackingapp.domain.entities.CourierLocation;
import org.example.couriertrackingapp.mappers.CourierMapper;
import org.example.couriertrackingapp.services.CourierTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourierTrackingServiceTest {
    @Spy
    private CourierMapper courierMapper;

    @InjectMocks
    private CourierTrackingService courierTrackingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalTravelDistance_initiallyEmpty() {
        UUID courierId = UUID.randomUUID();
        Double distance = courierTrackingService.getTotalTravelDistance(courierId);
        assertEquals(0.0, distance, "The total travel distance should be 0.0 when no courier locations are present.");
    }

    @Test
    void testSaveCourierLocation_shouldInvokeMapperAndUpdateState() {

        CourierLocationRequest request = new CourierLocationRequest();
        request.setCourierId(UUID.randomUUID());
        request.setLatitude(40.7128);
        request.setLongitude(-74.0060);

        CourierLocation mockedCourierLocation = new CourierLocation();
        mockedCourierLocation.setCourierId(request.getCourierId());
        mockedCourierLocation.setLatitude(request.getLatitude());
        mockedCourierLocation.setLongitude(request.getLongitude());
        mockedCourierLocation.setTimestamp(LocalDateTime.now());

        when(courierMapper.convertToEntity(request)).thenReturn(mockedCourierLocation);

        courierTrackingService.saveCourierLocation(request);

        verify(courierMapper, times(1)).convertToEntity(request);

        Double totalDistance = courierTrackingService.getTotalTravelDistance(request.getCourierId());
        assertEquals(0.0, totalDistance, "Total travel distance should still be 0 after saving the first location.");
    }

    @Test
    void testGetTotalTravelDistance_withLocations() {
        UUID courierId = UUID.randomUUID();
        CourierLocationRequest location1 = new CourierLocationRequest();
        location1.setCourierId(courierId);
        location1.setLatitude(40.7128);
        location1.setLongitude(-74.0060);

        CourierLocationRequest location2 = new CourierLocationRequest();
        location2.setCourierId(courierId);
        location2.setLatitude(34.0522);
        location2.setLongitude(-118.2437);

        CourierLocation mockedLocation1 = new CourierLocation();
        mockedLocation1.setCourierId(courierId);
        mockedLocation1.setLatitude(location1.getLatitude());
        mockedLocation1.setLongitude(location1.getLongitude());
        mockedLocation1.setTimestamp(LocalDateTime.now());

        CourierLocation mockedLocation2 = new CourierLocation();
        mockedLocation2.setCourierId(courierId);
        mockedLocation2.setLatitude(location2.getLatitude());
        mockedLocation2.setLongitude(location2.getLongitude());
        mockedLocation2.setTimestamp(LocalDateTime.now().plusMinutes(1));

        when(courierMapper.convertToEntity(location1)).thenReturn(mockedLocation1);
        when(courierMapper.convertToEntity(location2)).thenReturn(mockedLocation2);

        courierTrackingService.saveCourierLocation(location1);
        courierTrackingService.saveCourierLocation(location2);
        Double calculatedDistance = courierTrackingService.getTotalTravelDistance(courierId);

        assertEquals(true, calculatedDistance > 0, "Calculated distance between New York and Los Angeles should be greater than 0.");
    }

    @Test
    void testStoreEntryLoggedWhenWithinRadiusAndNoRecentEntry() {
        UUID courierId = UUID.randomUUID();

        CourierLocationRequest courierLocationRequest = new CourierLocationRequest();
        courierLocationRequest.setCourierId(courierId);
        courierLocationRequest.setLatitude(40.9923307);
        courierLocationRequest.setLongitude(29.1244229);

        CourierLocation courierLocation = new CourierLocation();
        courierLocation.setCourierId(courierId);
        courierLocation.setLatitude(40.9923307);
        courierLocation.setLongitude(29.1244229);
        courierLocation.setTimestamp(LocalDateTime.now());

        when(courierMapper.convertToEntity(any(CourierLocationRequest.class))).thenReturn(courierLocation);

        courierTrackingService.saveCourierLocation(courierLocationRequest);

        verify(courierMapper, times(1)).convertToEntity(courierLocationRequest);
    }

}
