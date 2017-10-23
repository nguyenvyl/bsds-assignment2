package com.programmerscuriosity.client;

import com.programmerscuriosity.model.RFIDLiftData;
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

public class MyPost implements Callable<Result>{

    private Result statistics;
    private String ipAddress;
    private List<RFIDLiftData> dataList;
    // TODO: Update URLs for post and get
    private static String POSTURL = "webapi/myresource/load";


    public MyPost(List<RFIDLiftData> data, String ip) {
        statistics = new Result();
        dataList = data;
        ipAddress = ip;
    }


    //call POST method
    public Response callPOST(WebTarget target, RFIDLiftData data) {
        double threadStartTime = System.currentTimeMillis();
        Response response = null;
        try {
            response = target.path(POSTURL).request()
                    .post(Entity.entity(data, MediaType.APPLICATION_JSON));
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
        for (RFIDLiftData data : dataList) {
            //calling POST
            Response response1 = callPOST(target, data);
            statistics.addNumberRequest();
            if(response1.getStatus() == 200) {
                statistics.addSuccessfulRequest();
            } 
        }
        client.close();
        return statistics;
    }
}