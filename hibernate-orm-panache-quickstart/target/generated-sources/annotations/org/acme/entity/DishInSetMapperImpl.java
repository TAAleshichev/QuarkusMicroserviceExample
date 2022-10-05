package org.acme.entity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.acme.resource.DishInSetPOJO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-06T00:07:24+0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
@ApplicationScoped
public class DishInSetMapperImpl implements DishInSetMapper {

    @Override
    public List<DishInSetPOJO> toDomainList(List<DishInSet> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DishInSetPOJO> list = new ArrayList<DishInSetPOJO>( entities.size() );
        for ( DishInSet dishInSet : entities ) {
            list.add( toDomain( dishInSet ) );
        }

        return list;
    }

    @Override
    public DishInSetPOJO toDomain(DishInSet entity) {
        if ( entity == null ) {
            return null;
        }

        DishInSetPOJO dishInSetPOJO = new DishInSetPOJO();

        dishInSetPOJO.setId( entity.id );
        dishInSetPOJO.setFoodset( entity.foodset );
        dishInSetPOJO.setDish( entity.dish );
        dishInSetPOJO.setNote( entity.note );

        return dishInSetPOJO;
    }

    @Override
    public DishInSet toEntity(DishInSetPOJO domain) {
        if ( domain == null ) {
            return null;
        }

        DishInSet.DishInSetBuilder dishInSet = DishInSet.builder();

        dishInSet.id( domain.getId() );
        dishInSet.foodset( domain.getFoodset() );
        dishInSet.dish( domain.getDish() );
        dishInSet.note( domain.getNote() );

        return dishInSet.build();
    }

    @Override
    public void updateEntityFromDomain(DishInSetPOJO domain, DishInSet entity) {
        if ( domain == null ) {
            return;
        }

        if ( domain.getId() != null ) {
            entity.id = domain.getId();
        }
        if ( domain.getFoodset() != null ) {
            entity.foodset = domain.getFoodset();
        }
        if ( domain.getDish() != null ) {
            entity.dish = domain.getDish();
        }
        if ( domain.getNote() != null ) {
            entity.note = domain.getNote();
        }
    }

    @Override
    public void updateDomainFromEntity(DishInSet entity, DishInSetPOJO domain) {
        if ( entity == null ) {
            return;
        }

        if ( entity.id != null ) {
            domain.setId( entity.id );
        }
        if ( entity.foodset != null ) {
            domain.setFoodset( entity.foodset );
        }
        if ( entity.dish != null ) {
            domain.setDish( entity.dish );
        }
        if ( entity.note != null ) {
            domain.setNote( entity.note );
        }
    }
}
