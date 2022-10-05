
package org.acme.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class MealtypePOJO {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

}
