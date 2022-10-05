package org.acme.entity;
import org.acme.resource.DishInSetPOJO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(config = Mappable.class)
public interface DishInSetMapper {

    List<DishInSetPOJO> toDomainList(List<DishInSet> entities);

    DishInSetPOJO toDomain(DishInSet entity);

    @InheritInverseConfiguration(name = "toDomain")
    DishInSet toEntity(DishInSetPOJO domain);

    void updateEntityFromDomain(DishInSetPOJO domain, @MappingTarget DishInSet entity);

    void updateDomainFromEntity(DishInSet entity, @MappingTarget DishInSetPOJO domain);
    
}

