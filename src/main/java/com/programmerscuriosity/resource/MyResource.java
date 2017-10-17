package com.programmerscuriosity.resource;

import com.programmerscuriosity.model.RFIDLiftData;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

@Path("myresource")
public class MyResource {
    public static ConcurrentLinkedQueue<RFIDLiftData> rawData = new ConcurrentLinkedQueue();
    
//    static {
//        MessageProcessor.startMessageProcessor();
//    }
    
    /**
     * To retrieve a message
     *
     * @return a string
     */
    @GET
    @Path("myvert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt(
            @QueryParam("skierID") int skierID,
            @QueryParam("dayNum") int dayNum) 
    {
        
        Response response = Response.status(Response.Status.OK).build();
        return response;
        
    }

    /**
     * Consumes a post request and stores the data into a queue. 
     *
     * @param message a message
     * @return a Response
     */
    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postIt(RFIDLiftData liftData) {
        rawData.add(liftData);
        Response response = Response.status(Response.Status.OK).build();
        return response;
    }
}
