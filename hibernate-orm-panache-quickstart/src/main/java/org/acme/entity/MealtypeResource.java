package org.acme.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.resource.DishResource;
import org.acme.resource.MealtypePOJO;
import org.acme.service.MealtypeService;
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

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.Objects;

@Path("/mealtypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "mealtype", description = "Mealtype Operations")
@AllArgsConstructor
@Slf4j
public class MealtypeResource {

    @Inject
    private MealtypeService mealtypeService;

    @GET
    @Counted(name = "Mealtype_list_return_count", description = "How many times Mealtype list was returned")
    @Timed(name = "Mealtype_list_return_time", description = "A measure of how long it takes to return a list of Mealtypes.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "200",
            description = "Get All Mealtypes",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Mealtype.class)
            )
    )
    public Response get() {
        return Response.ok(mealtypeService.findAll()).build();
    }

    @GET
    @Counted(name = "Mealtype_id_find_count", description = "How many Mealtypes have been found by id")
    @Timed(name = "Mealtype_id_find_time", description = "A measure of how long it takes to find a Mealtype.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "200",
            description = "Get Mealtype by id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Mealtype.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Mealtype does not exist for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        return mealtypeService.findById(id)
                .map(mealtype -> Response.ok(mealtype).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Counted(name = "Mealtype_post_count", description = "How many Mealtypes have been created")
    @Timed(name = "Mealtype_post_time", description = "A measure of how long it takes to create a Mealtype.", unit = MetricUnits.MILLISECONDS)
    @APIResponse(
            responseCode = "201",
            description = "Mealtype Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Mealtype.class)
            )
    )
    @APIResponse(
            responseCode = "500",
            description = "Invalid Mealtype",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Mealtype already exists for id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid MealtypePOJO mealtype, @Context UriInfo uriInfo) {
        mealtypeService.save(mealtype);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(mealtype.getId())).build();
        return Response.created(uri).entity(mealtype).build();
    }

    @PUT
    @Counted(name = "Mealtype_put_count", description = "How many Mealtypes have been updated")
    @Timed(name = "Mealtype_put_time", description = "A measure of how long it takes to update a Mealtype.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "204",
            description = "Mealtype updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Mealtype.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Mealtype",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Mealtype object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Mealtype.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Mealtype found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "id", required = true) @PathParam("id") Long id, @NotNull @Valid MealtypePOJO mealtype) {
        mealtypeService.update(mealtype);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Counted(name = "Mealtype_delete_count", description = "How many Mealtypes have been deleted")
    @Timed(name = "Mealtype_delete_time", description = "A measure of how long it takes to delete a Mealtype.", unit = MetricUnits.MILLISECONDS)
    @Path("{id}")
    @APIResponse(
            responseCode = "204",
            description = "Mealtype deleted",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Mealtype.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Mealtype",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Mealtype object does not have id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable id does not match Mealtype.id",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Mealtype found for id provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response delete(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        mealtypeService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
