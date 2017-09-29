package com.programmerscuriosity;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;

public class MyTask implements Callable<Result>{

    private int iterationTimes;
    private Result statistics;
    private String ipAddress;
    private static String GETURL = "SimpleJersey_war/rest/myresource";
    private static String POSTURL = "SimpleJersey_war/rest/myresource/post";


    public MyTask(int iteratedTimes, String ip) {
        iterationTimes = iteratedTimes;
        statistics = new Result();
        ipAddress = ip;
    }

    //call GET method
    public Response callGET(WebTarget target) {
        double threadStartTime = System.currentTimeMillis();
        Response response = null;
        try {
            response =  target.path(GETURL).
                    request().
                    accept(MediaType.TEXT_PLAIN).
                    get(Response.class);
            double threadEndTime = System.currentTimeMillis();
            response.close();
            double latency = threadEndTime - threadStartTime;
            statistics.getLatency().add(latency);
        }  catch (ProcessingException e) {
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Stack trace: " + e.getStackTrace());
        } catch (OutOfMemoryError e) {
            System.out.println("You don't have enough memory");
        }
        return response;
    }

    //call POST method
    public Response callPOST(WebTarget target) {
        double threadStartTime = System.currentTimeMillis();
        Response response = null;
        try {
            response = target.path(POSTURL).request()
                    .post(Entity.entity("Hello", MediaType.TEXT_PLAIN));
            double threadEndTime = System.currentTimeMillis();
            double latency = threadEndTime - threadStartTime;
            statistics.getLatency().add(latency);
            response.close();
        } catch (ProcessingException e) {
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Stack trace: " + e.getStackTrace());
        } catch (OutOfMemoryError e) {
            System.out.println("You don't have enough memory");
        }
        return response;
    }

    // function that each thread will call
    public Result call() throws Exception {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(ipAddress);
        for (int i = 0; i < this.iterationTimes; i++) {
            //calling GET
            Response response = callGET(target);
            //increase numbers of request
            statistics.addNumberRequest();
            //increase numbers of successful request if succeed
            if(response.getStatus() == 200) {
                statistics.addSuccessfulRequest();

            }
            //calling POST
            Response response1 = callPOST(target);
            statistics.addNumberRequest();
            if(response1.getStatus() == 201) {
                statistics.addSuccessfulRequest();
            }
        }
        client.close();
        return statistics;
    }
}