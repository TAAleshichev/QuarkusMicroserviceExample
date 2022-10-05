package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.acme.entity.DishInSet;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DishInSetRepository implements PanacheRepositoryBase<DishInSet, Long> {

}