package org.acme.entity;
import org.acme.resource.DishPOJO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(config = Mappable.class)
public interface DishMapper {

    List<DishPOJO> toDomainList(List<Dish> entities);

    DishPOJO toDomain(Dish entity);

    @InheritInverseConfiguration(name = "toDomain")
    Dish toEntity(DishPOJO domain);

    void updateEntityFromDomain(DishPOJO domain, @MappingTarget Dish entity);

    void updateDomainFromEntity(Dish entity, @MappingTarget DishPOJO domain);
    
}

