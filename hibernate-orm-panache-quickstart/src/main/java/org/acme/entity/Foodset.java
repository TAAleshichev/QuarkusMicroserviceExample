package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Table(name = "foodsets")
@Entity(name = "foodset")
@Cacheable
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Foodset extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FOODSET_SEQ")
    @SequenceGenerator(name = "SQ_FOODSET_SEQ", sequenceName = "SQ_FOODSET_SEQ", allocationSize = 10)
    public Long id;

    //@NotBlank(message="Meal type may not be blank")
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mealtype_id")
    public Mealtype mealtype;

    @NotBlank(message="Name of the set may not be blank")
    @NotNull
    @Column
    public String name;

    @Column(columnDefinition = "double precision check (total>=0)")
    public Double total;

    @OneToMany(mappedBy = "foodset", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public Set<DishInSet> dishesinsets = new HashSet<>();

    public void addDishInSet(DishInSet dis){
        dis.foodset = this;
        this.dishesinsets.add(dis);
        this.calculateTotal();
    }

    public void removeDishInSet(DishInSet dis){
        this.dishesinsets.remove(dis);
        dis.foodset = null;
        dis.dish = null;
        this.calculateTotal();
    }

    public void editComment(DishInSet dis, String newcomment){
        for (DishInSet needed : dishesinsets){
            if (needed == dis){
                needed.note = newcomment;
            }
        }
    }

    public void calculateTotal(){
        total = this.dishesinsets.stream().mapToDouble(DishInSet::getDishCost).sum();
    }

}
