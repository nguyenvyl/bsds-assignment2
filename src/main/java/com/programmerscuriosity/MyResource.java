package com.programmerscuriosity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("myresource")
public class MyResource {


    /**
     * To retrieve a message
     * @return a string
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    /**
     * Create a message
     * @param message a message
     * @return a Response
     */
    @POST
    @Path("/post")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response postIt(String message) {
        String result = "Message created: " + message;
        return Response.status(201).entity(result).build();
    }
}