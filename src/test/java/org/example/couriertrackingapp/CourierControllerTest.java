package org.example.couriertrackingapp;

import org.example.couriertrackingapp.domain.dtos.CourierLocationRequest;
import org.example.couriertrackingapp.endpoints.CourierController;
import org.example.couriertrackingapp.services.CourierTrackingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourierController.class)
public class CourierControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourierTrackingService courierTrackingService;

    @Test
    void testAddLocation_shouldReturnOk() throws Exception {
        String requestBody = """
            {
                "courierId": "123e4567-e89b-12d3-a456-426614174000",
                "latitude": 40.7128,
                "longitude": -74.0060
            }
        """;

        doNothing().when(courierTrackingService).saveCourierLocation(Mockito.any(CourierLocationRequest.class));

        mockMvc.perform(post("/v1/api/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk());

        verify(courierTrackingService, times(1)).saveCourierLocation(Mockito.any(CourierLocationRequest.class));
    }

    @Test
    void testGetTotalTravelDistance_shouldReturnDistance() throws Exception {
        UUID courierId = UUID.randomUUID();
        Double expectedDistance = 15.75;

        when(courierTrackingService.getTotalTravelDistance(courierId)).thenReturn(expectedDistance);

        mockMvc.perform(get("/v1/api/courier/" + courierId + "/total-distance")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedDistance.toString()));

        verify(courierTrackingService, times(1)).getTotalTravelDistance(courierId);
    }
}
