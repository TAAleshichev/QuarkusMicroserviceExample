package org.acme.entity;
import org.acme.resource.FoodsetPOJO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(config = Mappable.class)
public interface FoodsetMapper {

    List<FoodsetPOJO> toDomainList(List<Foodset> entities);

    FoodsetPOJO toDomain(Foodset entity);

    @InheritInverseConfiguration(name = "toDomain")
    Foodset toEntity(FoodsetPOJO domain);

    void updateEntityFromDomain(FoodsetPOJO domain, @MappingTarget Foodset entity);

    void updateDomainFromEntity(Foodset entity, @MappingTarget FoodsetPOJO domain);
    
}

