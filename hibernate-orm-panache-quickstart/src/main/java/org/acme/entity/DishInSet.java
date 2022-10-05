package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "dishes_in_sets")
@Entity(name = "dish_in_set")
@Cacheable
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DishInSet extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DISHINSET_SEQ")
    @SequenceGenerator(name = "SQ_DISHINSET_SEQ", sequenceName = "SQ_DISHINSET_SEQ", allocationSize = 10)
    public Long id;

    //@NotBlank(message="Food set may not be blank")
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "foodset_id")
    @JsonIgnore
    public Foodset foodset;

    //@NotBlank(message="Dish may not be blank")
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id")
    public Dish dish;

    @JsonIgnore
    public double getDishCost(){
        return dish.cost;
    }

    @Column()
    public String note;

}
