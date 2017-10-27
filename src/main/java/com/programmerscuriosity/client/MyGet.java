package com.programmerscuriosity.client;

import com.programmerscuriosity.model.SkierData;
import java.util.Arrays;
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
    private int numIterations;
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
    public SkierData callGET(String ipAddress, int dayNum, int skierID) {
        double threadStartTime = System.currentTimeMillis();
        SkierData skier = null;
        String getURL = addQueryParams(ipAddress, GETURL, dayNum, skierID);
        Client client = ClientBuilder.newClient();
        try {
            skier = client.target(getURL)
                    .request(MediaType.APPLICATION_JSON)
                    .get(SkierData.class);
            double threadEndTime = System.currentTimeMillis();
            double latency = threadEndTime - threadStartTime;
            statistics.getLatency().add(latency);
        }  catch (ProcessingException e) {
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Stack trace: " + Arrays.toString(e.getStackTrace()));
        } catch (OutOfMemoryError e) {
            System.out.println("You don't have enough memory");
        } catch (Exception e) {
            return skier;
        }
        System.out.println("Retrieved skier " + skier.toSQLString());
        return skier;
    }
    
    public String addQueryParams(String ipAddress, String URL, int dayNum, int skierID) {
        return ipAddress + URL + "?dayNum=" + Integer.toString(dayNum) + "&skierID=" + Integer.toString(skierID);
    }

    // function that each thread will call
    public Result call() throws Exception {
        for (int i = startID; i < startID + numIterations; i++) {
           
//            calling GET
            SkierData skier = callGET(ipAddress, dayNum, i);
//            increase numbers of request
            statistics.addNumberRequest();
//            increase numbers of successful request if succeed
            if(skier != null) {
                statistics.addSuccessfulRequest();
            }
        }
        return statistics;
    }
}