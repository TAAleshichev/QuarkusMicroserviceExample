package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.acme.entity.Mealtype;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MealtypeRepository implements PanacheRepositoryBase<Mealtype, Long> {

}