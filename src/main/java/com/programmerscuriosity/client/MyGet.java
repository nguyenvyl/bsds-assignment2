package com.programmerscuriosity.client;

import com.programmerscuriosity.model.SkierData;
import java.util.Arrays;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.Callable;

public class MyGet implements Callable<Result>{

    private final Result statistics;
    private final String ipAddress;
    private final int dayNum;
    private final int startID;
    private final int numIterations;
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
        String getURL = createGETUrl(ipAddress, GETURL, dayNum, skierID);
        Client client = ClientBuilder.newClient();
        try {
            skier = client.target(getURL)
                    .request(MediaType.APPLICATION_JSON)
                    .get(SkierData.class);
            double threadEndTime = System.currentTimeMillis();
            double latency = threadEndTime - threadStartTime;
            statistics.getLatency().add(latency);
        }  catch (ProcessingException e) {
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Stack trace: " + Arrays.toString(e.getStackTrace()));
        } catch (OutOfMemoryError e) {
            System.err.println("You don't have enough memory");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return skier;
    }
    
    // Construct GET URL with query params
    public String createGETUrl(String ipAddress, String URL, int dayNum, int skierID) {
        return ipAddress + URL + "?dayNum=" + Integer.toString(dayNum) + "&skierID=" + Integer.toString(skierID);
    }

    // function that each thread will call
    @Override
    public Result call() throws Exception {
        for (int i = startID; i < startID + numIterations; i++) {
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