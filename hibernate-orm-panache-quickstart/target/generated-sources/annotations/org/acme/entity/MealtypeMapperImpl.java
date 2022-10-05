package org.acme.entity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.acme.resource.MealtypePOJO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-06T00:07:23+0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
@ApplicationScoped
public class MealtypeMapperImpl implements MealtypeMapper {

    @Override
    public List<MealtypePOJO> toDomainList(List<Mealtype> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MealtypePOJO> list = new ArrayList<MealtypePOJO>( entities.size() );
        for ( Mealtype mealtype : entities ) {
            list.add( toDomain( mealtype ) );
        }

        return list;
    }

    @Override
    public MealtypePOJO toDomain(Mealtype entity) {
        if ( entity == null ) {
            return null;
        }

        MealtypePOJO mealtypePOJO = new MealtypePOJO();

        mealtypePOJO.setId( entity.id );
        mealtypePOJO.setName( entity.name );

        return mealtypePOJO;
    }

    @Override
    public Mealtype toEntity(MealtypePOJO domain) {
        if ( domain == null ) {
            return null;
        }

        Mealtype.MealtypeBuilder mealtype = Mealtype.builder();

        mealtype.id( domain.getId() );
        mealtype.name( domain.getName() );

        return mealtype.build();
    }

    @Override
    public void updateEntityFromDomain(MealtypePOJO domain, Mealtype entity) {
        if ( domain == null ) {
            return;
        }

        if ( domain.getId() != null ) {
            entity.id = domain.getId();
        }
        if ( domain.getName() != null ) {
            entity.name = domain.getName();
        }
    }

    @Override
    public void updateDomainFromEntity(Mealtype entity, MealtypePOJO domain) {
        if ( entity == null ) {
            return;
        }

        if ( entity.id != null ) {
            domain.setId( entity.id );
        }
        if ( entity.name != null ) {
            domain.setName( entity.name );
        }
    }
}
