package com.programmerscuriosity.client;

import java.util.List;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;
import org.json.JSONObject;

public class MyGet implements Callable<Result>{

    private Result statistics;
    private String ipAddress;
    private int dayNum;
    private int startID;
    private int numIterations = 400;
    // TODO: Update URLs for post and get
    private static final String GETURL = "webapi/myresource/myvert";


    public MyGet(String ip, int dayNum, int numIterations, int startID) {
        this.startID = startID;
        this.numIterations = numIterations;
        this.dayNum = dayNum;
        statistics = new Result();
        ipAddress = ip;
    }

    //call GET method
    public Response callGET(WebTarget target, int dayNum, int skierID) {
        double threadStartTime = System.currentTimeMillis();
        Response response = null;
        String getURL = addQueryParams(GETURL, dayNum, skierID);
        try {
            response =  target.path(getURL)
                    .request()
                    .accept(MediaType.TEXT_PLAIN).
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
    
    public String addQueryParams(String URL, int dayNum, int skierID) {
        return URL + "?dayNum=" + Integer.toString(dayNum) + "&skierID=" + Integer.toString(skierID);
    }

    // function that each thread will call
    public Result call() throws Exception {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(ipAddress);
        for (int i = startID; i < numIterations; i++) {
//            calling GET
            Response response = callGET(target, dayNum, i);
//            increase numbers of request
            statistics.addNumberRequest();
//            increase numbers of successful request if succeed
            if(response.getStatus() == 200) {
                statistics.addSuccessfulRequest();
            }
        }
        client.close();
        return statistics;
    }
}