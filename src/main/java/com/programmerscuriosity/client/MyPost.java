package com.programmerscuriosity.client;

import com.programmerscuriosity.model.RFIDLiftData;
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
import jersey.repackaged.com.google.common.collect.Lists;
import org.glassfish.jersey.client.ClientProperties;

public class MyPost implements Callable<Result>{

    private final Result statistics;
    private final String ipAddress;
    private final List<RFIDLiftData> dataList;
    private final int NUM_REQUESTS = 100;
    private final static String BATCH_POSTURL = "webapi/myresource/loadBatch";


    public MyPost(List<RFIDLiftData> data, String ip) {
        statistics = new Result();
        dataList = data;
        ipAddress = ip;
    }

    
    //call batch POST method
    public Response callBatchPOST(WebTarget target, List<RFIDLiftData> data) {
        double threadStartTime = System.currentTimeMillis();
        Response response = null;
        try {
            response = target.path(BATCH_POSTURL).request()
                    .post(Entity.entity(data, MediaType.APPLICATION_JSON));
            double threadEndTime = System.currentTimeMillis();
            double latency = threadEndTime - threadStartTime;
            statistics.getLatency().add(latency);
            response.close();
        } catch (ProcessingException e) {
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Stack trace: " + Arrays.toString(e.getStackTrace()));
        } catch (OutOfMemoryError e) {
            System.out.println("You don't have enough memory");
        }
        return response;
    }

    public Result call() throws Exception {
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, 5000);
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(ipAddress);
        List<List<RFIDLiftData>> dataBatches = Lists.partition(dataList, NUM_REQUESTS);
        for (List<RFIDLiftData> dataBatch : dataBatches) {
            //calling POST
            Response response1 = callBatchPOST(target, dataBatch);
            statistics.addNumberRequest();
            if(response1.getStatus() == 200) {
                statistics.addSuccessfulRequest();
            } 
        }
        client.close();
        return statistics;
    }
    
    
    
}
