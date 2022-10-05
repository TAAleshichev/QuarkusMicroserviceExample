package org.acme.resource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.Dish;
import org.acme.entity.DishInSet;
import org.acme.entity.DishInSet;
import org.acme.entity.DishInSet;
import org.acme.service.DishInSetService;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Path("dishesinsets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "dishinset", description = "Dishinset Operations")
@AllArgsConstructor
@Slf4j
public class DishInSetResource {

    @Inject
    private DishInSetService dishinsetService;

    @GET
    @Counted(name = "DishInSet_list_return_count", description = "How many times Dish list was returned")
    @Timed(name = "DishInSet_list_return_time", description = "A measure of how long it takes to return a list of DishesInSets.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "200",
            description = "Get All DishesInSets",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = DishInSet.class)
            )
    )
    public Response get() {
        return Response.ok(dishinsetService.findAll()).build();
    }

    @GET
    @Counted(name = "DishInSet_id_find_count", description = "How many DishesInSets have been found by id")
    @Timed(name = "DishInSet_id_find_time", description = "A measure of how long it takes to find a DishInSet.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "200",
            description = "Get DishInSet by id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = DishInSet.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "DishInSet does not exist for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        return dishinsetService.findById(id)
                .map(dishinset -> Response.ok(dishinset).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


}
