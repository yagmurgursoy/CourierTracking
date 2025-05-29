package org.example.couriertrackingapp.mappers;

import org.example.couriertrackingapp.domain.entities.Store;
import org.example.couriertrackingapp.domain.dtos.StoreRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper extends BaseMapper<Store, StoreRequest> {
}
