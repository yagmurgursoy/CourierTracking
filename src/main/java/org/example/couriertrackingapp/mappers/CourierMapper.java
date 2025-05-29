package org.example.couriertrackingapp.mappers;

import org.example.couriertrackingapp.domain.entities.CourierLocation;
import org.example.couriertrackingapp.domain.dtos.CourierLocationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourierMapper extends BaseMapper<CourierLocation, CourierLocationRequest> {

}
