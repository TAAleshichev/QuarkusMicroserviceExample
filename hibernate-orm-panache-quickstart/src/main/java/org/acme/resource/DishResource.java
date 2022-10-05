package org.acme.resource;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.Dish;
import org.acme.service.DishService;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;

@Path("dishes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "dish", description = "Dish Operations")
@AllArgsConstructor
@Slf4j
public class DishResource {

    @Inject
    private DishService dishService;

    @GET
    @Counted(name = "Dish_list_return_count", description = "How many times Dish list was returned")
    @Timed(name = "Dish_list_return_time", description = "A measure of how long it takes to return a list of Dishes.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "200",
            description = "Get All Dishes",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Dish.class)
            )
    )
    public Response get() {
        return Response.ok(dishService.findAll()).build();
    }

    @GET
    @Counted(name = "Dish_id_find_count", description = "How many Dishes have been found by id")
    @Timed(name = "Dish_id_find_time", description = "A measure of how long it takes to find a Dish.", unit = MetricUnits.MILLISECONDS)
    @Path("/{id}")
    @APIResponse(
            responseCode = "200",
            description = "Get Dish by id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Dish.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Dish does not exist for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        return dishService.findById(id)
                .map(dish -> Response.ok(dish).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Counted(name = "Dish_post_count", description = "How many Dishes have been created")
    @Timed(name = "Dish_post_time", description = "A measure of how long it takes to create a Dish.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "201",
            description = "Dish Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Dish.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Dish",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Dish already exists for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid DishPOJO dish, @Context UriInfo uriInfo) {
        dishService.save(dish);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(dish.getId())).build();
        return Response.created(uri).entity(dish).build();
    }

    @PUT
    @Counted(name = "Dish_put_count", description = "How many Dishes have been updated")
    @Timed(name = "Dish_put_time", description = "A measure of how long it takes to update a Dish.", unit = MetricUnits.MILLISECONDS)
    @Path("/{id}")
    @APIResponse(
            responseCode = "204",
            description = "Dish updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Dish.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Dish",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Dish object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Dish.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Dish found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "id", required = true) @PathParam("id") Long id, @NotNull @Valid DishPOJO dish) {
        dishService.update(dish);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Counted(name = "Dish_delete_count", description = "How many Dishes have been deleted")
    @Timed(name = "Dish_delete_time", description = "A measure of how long it takes to delete a Dish.", unit = MetricUnits.MILLISECONDS)
    @Path("/{id}")
    @APIResponse(
            responseCode = "204",
            description = "Dish deleted",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Dish.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Dish",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Dish object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Dish.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Dish found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response delete(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        dishService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
