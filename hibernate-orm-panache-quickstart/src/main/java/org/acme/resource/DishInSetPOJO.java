
package org.acme.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.acme.entity.Dish;
import org.acme.entity.Foodset;

import javax.persistence.*;


@Data
public class DishInSetPOJO {

    private Long id;
    private Foodset foodset;
    private Dish dish;
    private String note;

}
