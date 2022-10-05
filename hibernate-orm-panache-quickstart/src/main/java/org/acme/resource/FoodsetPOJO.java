
package org.acme.resource;

import lombok.Data;
import org.acme.entity.DishInSet;
import org.acme.entity.Mealtype;
import org.acme.entity.MealtypeMapper;

import java.util.HashSet;
import java.util.Set;


@Data
public class FoodsetPOJO {

    private Long id;
    private Mealtype mealtype;
    private String name;
    private Double total;
    private Set<DishInSet> dishesinsets = new HashSet<>();

}
