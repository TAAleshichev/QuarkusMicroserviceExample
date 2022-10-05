package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.acme.entity.Dish;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DishRepository implements PanacheRepositoryBase<Dish, Long> {

}