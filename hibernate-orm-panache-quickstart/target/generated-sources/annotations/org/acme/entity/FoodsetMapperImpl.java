package org.acme.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.acme.resource.FoodsetPOJO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-06T00:07:23+0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
@ApplicationScoped
public class FoodsetMapperImpl implements FoodsetMapper {

    @Override
    public List<FoodsetPOJO> toDomainList(List<Foodset> entities) {
        if ( entities == null ) {
            return null;
        }

        List<FoodsetPOJO> list = new ArrayList<FoodsetPOJO>( entities.size() );
        for ( Foodset foodset : entities ) {
            list.add( toDomain( foodset ) );
        }

        return list;
    }

    @Override
    public FoodsetPOJO toDomain(Foodset entity) {
        if ( entity == null ) {
            return null;
        }

        FoodsetPOJO foodsetPOJO = new FoodsetPOJO();

        foodsetPOJO.setId( entity.id );
        foodsetPOJO.setMealtype( entity.mealtype );
        foodsetPOJO.setName( entity.name );
        foodsetPOJO.setTotal( entity.total );
        Set<DishInSet> set = entity.dishesinsets;
        if ( set != null ) {
            foodsetPOJO.setDishesinsets( new LinkedHashSet<DishInSet>( set ) );
        }

        return foodsetPOJO;
    }

    @Override
    public Foodset toEntity(FoodsetPOJO domain) {
        if ( domain == null ) {
            return null;
        }

        Foodset.FoodsetBuilder foodset = Foodset.builder();

        foodset.id( domain.getId() );
        foodset.mealtype( domain.getMealtype() );
        foodset.name( domain.getName() );
        foodset.total( domain.getTotal() );
        Set<DishInSet> set = domain.getDishesinsets();
        if ( set != null ) {
            foodset.dishesinsets( new LinkedHashSet<DishInSet>( set ) );
        }

        return foodset.build();
    }

    @Override
    public void updateEntityFromDomain(FoodsetPOJO domain, Foodset entity) {
        if ( domain == null ) {
            return;
        }

        if ( domain.getId() != null ) {
            entity.id = domain.getId();
        }
        if ( domain.getMealtype() != null ) {
            entity.mealtype = domain.getMealtype();
        }
        if ( domain.getName() != null ) {
            entity.name = domain.getName();
        }
        if ( domain.getTotal() != null ) {
            entity.total = domain.getTotal();
        }
        if ( entity.dishesinsets != null ) {
            Set<DishInSet> set = domain.getDishesinsets();
            if ( set != null ) {
                entity.dishesinsets.clear();
                entity.dishesinsets.addAll( set );
            }
        }
        else {
            Set<DishInSet> set = domain.getDishesinsets();
            if ( set != null ) {
                entity.dishesinsets = new LinkedHashSet<DishInSet>( set );
            }
        }
    }

    @Override
    public void updateDomainFromEntity(Foodset entity, FoodsetPOJO domain) {
        if ( entity == null ) {
            return;
        }

        if ( entity.id != null ) {
            domain.setId( entity.id );
        }
        if ( entity.mealtype != null ) {
            domain.setMealtype( entity.mealtype );
        }
        if ( entity.name != null ) {
            domain.setName( entity.name );
        }
        if ( entity.total != null ) {
            domain.setTotal( entity.total );
        }
        if ( domain.getDishesinsets() != null ) {
            Set<DishInSet> set = entity.dishesinsets;
            if ( set != null ) {
                domain.getDishesinsets().clear();
                domain.getDishesinsets().addAll( set );
            }
        }
        else {
            Set<DishInSet> set = entity.dishesinsets;
            if ( set != null ) {
                domain.setDishesinsets( new LinkedHashSet<DishInSet>( set ) );
            }
        }
    }
}
