package org.acme.entity;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Table(name = "meal_types")
@Entity(name = "mealtype")
@Cacheable
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Mealtype extends PanacheEntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_MEALTYPE_SEQ")
    @SequenceGenerator(name = "SQ_MEALTYPE_SEQ", sequenceName = "SQ_MEALTYPE_SEQ", allocationSize = 10)
    public Long id;

    @NotBlank(message="Name of the Meal type may not be blank")
    @Column(length = 40, unique = true)
    public String name;


}
