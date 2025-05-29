package org.example.couriertrackingapp.endpoints;

import lombok.RequiredArgsConstructor;
import org.example.couriertrackingapp.domain.dtos.CourierLocationRequest;
import org.example.couriertrackingapp.services.CourierTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/courier")
public class CourierController {

    private final CourierTrackingService courierTrackingService;

    @PostMapping("/location")
    public ResponseEntity<Void> addlocation(@RequestBody CourierLocationRequest request) {
       courierTrackingService.saveCourierLocation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courierId}/total-distance")
    public ResponseEntity<Double> getTotalTravelDistance(@PathVariable UUID courierId) {
        Double distance = courierTrackingService.getTotalTravelDistance(courierId);
        return ResponseEntity.ok(distance);
    }
}
