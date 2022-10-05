package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import org.acme.entity.Foodset;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class FoodsetRepository implements PanacheRepositoryBase<Foodset, Long> {

    public List<Foodset> findAndSort(){
        return findAll(Sort.by("total").and("name")).list();
    }

    //public List<Foodset> findHowManyItems(){
        //return find("#foodset.getAmounts").list();
    //}

}