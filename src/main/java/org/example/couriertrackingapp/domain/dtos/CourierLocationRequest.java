package org.example.couriertrackingapp.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationRequest {
    private UUID courierId;
    private Double latitude;
    private Double longitude;

}
