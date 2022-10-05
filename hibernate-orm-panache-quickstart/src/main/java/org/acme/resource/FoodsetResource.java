package org.acme.resource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.Dish;
import org.acme.entity.DishInSet;
import org.acme.entity.Foodset;
import org.acme.entity.Foodset;
import org.acme.service.FoodsetService;
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
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static javax.ws.rs.core.Response.Status.valueOf;

@Path("foodsets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "foodset", description = "Foodset Operations")
@AllArgsConstructor
@Slf4j
public class FoodsetResource {

    @Inject
    private FoodsetService foodsetService;

    @GET
    @Counted(name = "Foodset_list_return_count", description = "How many times Foodset list was returned")
    @Timed(name = "Foodset_list_return_time", description = "A measure of how long it takes to return a list of Foodsets.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "200",
            description = "Get All Foodsets",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Foodset.class)
            )
    )
    public Response get() {
        return Response.ok(foodsetService.findAll()).build();
    }

    @GET
    @Counted(name = "Foodset_id_find_count", description = "How many Foodsets have been found by id")
    @Timed(name = "Foodset_id_find_time", description = "A measure of how long it takes to find a Foodset.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "200",
            description = "Get Foodset by id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Foodset does not exist for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        return foodsetService.findById(id)
                .map(foodset -> Response.ok(foodset).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Counted(name = "Foodset_post_count", description = "How many Foodsets have been created")
    @Timed(name = "Foodset_post_time", description = "A measure of how long it takes to create a Foodset.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "201",
            description = "Foodset Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset already exists for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid FoodsetPOJO foodset, @Context UriInfo uriInfo) {
        foodsetService.save(foodset);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(foodset.getId())).build();
        return Response.created(uri).entity(foodset).build();
    }

    @POST
    @Counted(name = "Foodset_post_with_dish_count", description = "How many Foodsets with Dishes have been created")
    @Timed(name = "Foodset_post_with_dish_time", description = "A measure of how long it takes to create a Foodset with Dish.", unit = MetricUnits.MILLISECONDS)
    @Path("/post_with_dish/{dishid}/{note}")
    @APIResponse(
            responseCode = "201",
            description = "Foodset With a Dish and a Comment Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset already exists for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response savewithdish(@NotNull @Valid FoodsetPOJO foodset, Long dishid, @Context UriInfo uriInfo, @NotNull @Valid @PathParam("note") String note) {
        foodsetService.save(foodset);
        foodsetService.addFoodToSet(foodset.getId(), dishid, note);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(foodset.getId())).build();
        return Response.created(uri).entity(foodset).build();
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "x random Foodsets filled with 3 random Dishes Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset already exists for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Path("{x}")
    @Counted(name = "Foodset_post_x_count", description = "How many sets of x random Foodsets have been created")
    @Timed(name = "Foodset_post_x_time", description = "A measure of how long it takes to create x random Foodsets.", unit = MetricUnits.MILLISECONDS)
    public Response createxrandom(int x) {
        foodsetService.savexrandom(x);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    @PUT
    @Counted(name = "Foodset_put_count", description = "How many Foodsets have been updated")
    @Timed(name = "Foodset_put_time", description = "A measure of how long it takes to update a Foodset.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "204",
            description = "Foodset updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Foodset.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Foodset found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "id", required = true) @PathParam("id") Long id, @NotNull FoodsetPOJO foodset) {
        foodsetService.update(foodset);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Counted(name = "Foodset_delete_count", description = "How many Foodsets have been deleted")
    @Timed(name = "Foodset_delete_time", description = "A measure of how long it takes to delete a Foodset.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "204",
            description = "Foodset deleted",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Foodset.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Foodset found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response delete(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        foodsetService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }



    @PUT
    @Counted(name = "Foodset_add_dish_count", description = "How many Dishes have been added to Foodsets ")
    @Timed(name = "Foodset_add_dish_time", description = "A measure of how long it takes to add Dish into a Foodset.", unit = MetricUnits.MILLISECONDS)
    @Path("/add_dish/{id}/{dishid}/{note}")
    @APIResponse(
            responseCode = "204",
            description = "Dish with note added to a Foodset",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Foodset.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Foodset found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFoodToSet(Long id, @PathParam("dishid") Long dish, @PathParam("note") String note) {
        foodsetService.addFoodToSet(id, dish, note);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Counted(name = "Foodset_edit_dish_note_count", description = "How many notes of Dishes have been edited in Foodsets ")
    @Timed(name = "Foodset_edit_dish_note_time", description = "A measure of how long it takes to edit a Dish note in a Foodset.", unit = MetricUnits.MILLISECONDS)
    @Path("/edit_dish_note/{id}/{dishinsetid}/{note}")
    @APIResponse(
            responseCode = "204",
            description = "A note for a Dish in a Foodset updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Foodset.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Foodset found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response noteEdit(Long id, Long dishinsetid, @PathParam("note") String note) {
        foodsetService.commentEdit(id, dishinsetid, note);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @DELETE
    @Counted(name = "Foodset_remove_from_set_count", description = "How many Dishes have been removed from Foodsets ")
    @Timed(name = "Foodset_remove_from_set_time", description = "A measure of how long it takes to removed a Dish from a Foodset.", unit = MetricUnits.MILLISECONDS)
    @Path("/remove_from_set/{id}/{dishinsetid}")
    @APIResponse(
            responseCode = "204",
            description = "Dish removed from Foodset by id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Foodset.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Foodset",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Foodset object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Foodset.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Foodset found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFoodFromSet(Long id, Long dishinsetid) {
        foodsetService.removeFoodFromSet(id, dishinsetid);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
