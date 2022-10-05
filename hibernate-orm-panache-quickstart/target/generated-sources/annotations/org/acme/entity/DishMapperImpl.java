package org.acme.entity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.acme.resource.DishPOJO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-06T00:07:24+0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
@ApplicationScoped
public class DishMapperImpl implements DishMapper {

    @Override
    public List<DishPOJO> toDomainList(List<Dish> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DishPOJO> list = new ArrayList<DishPOJO>( entities.size() );
        for ( Dish dish : entities ) {
            list.add( toDomain( dish ) );
        }

        return list;
    }

    @Override
    public DishPOJO toDomain(Dish entity) {
        if ( entity == null ) {
            return null;
        }

        DishPOJO dishPOJO = new DishPOJO();

        dishPOJO.setId( entity.id );
        dishPOJO.setName( entity.name );
        dishPOJO.setCost( entity.cost );

        return dishPOJO;
    }

    @Override
    public Dish toEntity(DishPOJO domain) {
        if ( domain == null ) {
            return null;
        }

        Dish.DishBuilder dish = Dish.builder();

        dish.id( domain.getId() );
        dish.name( domain.getName() );
        dish.cost( domain.getCost() );

        return dish.build();
    }

    @Override
    public void updateEntityFromDomain(DishPOJO domain, Dish entity) {
        if ( domain == null ) {
            return;
        }

        if ( domain.getId() != null ) {
            entity.id = domain.getId();
        }
        if ( domain.getName() != null ) {
            entity.name = domain.getName();
        }
        if ( domain.getCost() != null ) {
            entity.cost = domain.getCost();
        }
    }

    @Override
    public void updateDomainFromEntity(Dish entity, DishPOJO domain) {
        if ( entity == null ) {
            return;
        }

        if ( entity.id != null ) {
            domain.setId( entity.id );
        }
        if ( entity.name != null ) {
            domain.setName( entity.name );
        }
        if ( entity.cost != null ) {
            domain.setCost( entity.cost );
        }
    }
}
