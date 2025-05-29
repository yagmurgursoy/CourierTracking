package org.example.couriertrackingapp.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {
    private String name;
    private double lat;
    private double lng;
}
