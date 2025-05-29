package org.example.couriertrackingapp.mappers;

import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

public interface BaseMapper<E, D> {
    E convertToEntity(D dto);
    D convert(E entity);
    List<D> convert(List<E> entities);
    Set<D> convert(Set<E> entities);
    void update(@MappingTarget E existingEntity, E entity);
}
