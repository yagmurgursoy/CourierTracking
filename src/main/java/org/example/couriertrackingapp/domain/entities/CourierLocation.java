package org.example.couriertrackingapp.domain.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocation {
    private UUID courierId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;

}
