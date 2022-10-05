package org.acme.entity;
import org.acme.resource.MealtypePOJO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(config = Mappable.class)
public interface MealtypeMapper {

    List<MealtypePOJO> toDomainList(List<Mealtype> entities);

    MealtypePOJO toDomain(Mealtype entity);

    @InheritInverseConfiguration(name = "toDomain")
    Mealtype toEntity(MealtypePOJO domain);

    void updateEntityFromDomain(MealtypePOJO domain, @MappingTarget Mealtype entity);

    void updateDomainFromEntity(Mealtype entity, @MappingTarget MealtypePOJO domain);
    
}

