package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "dishes")
@Entity(name = "dish")
@Cacheable
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dish extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DISH_SEQ")
    @SequenceGenerator(name = "SQ_DISH_SEQ", sequenceName = "SQ_DISH_SEQ", allocationSize = 10)
    public Long id;

    @NotBlank(message="Name of the Meal type may not be blank")
    @NotNull
    @Column(length = 40, unique = true)
    public String name;

    //@Min(message="Dish must cost at least 1 rouble", value=1)
    @Column(columnDefinition = "double precision check (cost>0)")
    public Double cost;

}
