package com.programmerscuriosity.client;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import com.google.common.collect.Lists;
import com.programmerscuriosity.model.RFIDLiftData;

public class MyClient {

    //default argument values
    private static final int NUMBER_POST_THREADS = 400;
    private static final int NUMBER_GET_THREADS = 100;
    private static final int NUM_SKIERS = 40000;
    private static final String IPADDRESS = "http://bsdsdatabase-env-3.us-west-2.elasticbeanstalk.com/";
    private static final String PORT = "8084";
    private static final String DAY1_FILE = "/home/nguyenvyl/NetBeansProjects/SDS/src/main/resources/BSDSAssignment2Day1.csv";
//    private static String IP = IPADDRESS.concat(":").concat(PORT);
    private static String IP = IPADDRESS;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, Exception {
        loadFileToDatabase(DAY1_FILE);

    }

    public static void loadFileToDatabase(String fileName) throws InterruptedException, ExecutionException, ExecutionException, IOException {
        ArrayList<RFIDLiftData> dataList = ReadData.readData(fileName);
        List<List<RFIDLiftData>> partitionedLists = Lists.partition(dataList, NUMBER_POST_THREADS);

        System.out.println("Start rolling ...");
        System.out.println("IP address of server is: " + IPADDRESS);
        System.out.println("Listening on port: " + PORT);
        System.out.println("Number of Thread: " + NUMBER_POST_THREADS);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_POST_THREADS);
//        Result statistics = new Result();
//        List<Future<Result>> results = new ArrayList<>();
//
        long startTime = System.currentTimeMillis();
        // begin threads
        for (List<RFIDLiftData> data : partitionedLists) {
            Future<Result> task = executor.submit(new MyPost(data, IP));
//            results.add(task);
        }
        executor.shutdown();
        // make sure all tasks are completed
        while (!executor.isTerminated()) {
        }
        System.out.println("All threads completed...");

        long stopTime = System.currentTimeMillis();
        long wallTime = stopTime - startTime;

        int numberRequests = 0;
        int successfulRequest = 0;
        List<Double> latencyList = new ArrayList<>();

//        for (Future<Result> result : results) {
//            numberRequests += result.get().getNumberRequests();
//            successfulRequest += result.get().getSuccessfulRequests();
//            latencyList.addAll(result.get().getLatency());
//        }

//        statistics.setLatency(latencyList);
//        statistics.setNumberRequests(numberRequests);
//        statistics.setSuccessfulRequests(successfulRequest);
//
//        System.out.println("Number of POST requests is: " + numberRequests);
//        System.out.println("Number of successful POST requests is: " + successfulRequest);
//        System.out.println("Time for all POST threads to run takes: " + wallTime + " milliseconds");
//
//        //get mean & median
//        Double meanLatencies = latencyList.stream().mapToDouble(l -> l).average().getAsDouble();
//        System.out.println("The mean latencies for all POST requests is: " + meanLatencies + " milliseconds");
//        Double median = getMedian(latencyList);
//        System.out.println("The median latencies for all POST requests is: " + median + " milliseconds");
//
//        //get 99th & 95th percentage
//        System.out.println("The 95th percentile POST latency is: " + latencyList.get((int) (latencyList.size() * 0.95)) + " milliseconds");
//        System.out.println("The 99th percentile POST latency is: " + latencyList.get((int) (latencyList.size() * 0.99)) + " milliseconds");
    }

    public static void getAllUserData(int dayNum) throws InterruptedException, ExecutionException, ExecutionException, IOException {

        System.out.println("Start rolling ...");
        System.out.println("IP address of server is: " + IPADDRESS);
        System.out.println("Listening on port: " + PORT);
        System.out.println("Number of Thread: " + NUMBER_POST_THREADS);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_GET_THREADS);
        Result statistics = new Result();
        List<Future<Result>> results = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        // begin threads
        
        int requestsPerThread = NUM_SKIERS / NUMBER_GET_THREADS;
        int startID = 1;
        for (int i = 0; i < NUMBER_GET_THREADS; i++) {
            Future<Result> task = executor.submit(new MyGet(IP, dayNum, requestsPerThread, startID));
            results.add(task);
            startID += requestsPerThread;
        }
        executor.shutdown();
        // make sure all tasks are completed
        while (!executor.isTerminated()) {
        }
        System.out.println("All threads completed...");

        long stopTime = System.currentTimeMillis();
        long wallTime = stopTime - startTime;

        int numberRequests = 0;
        int successfulRequest = 0;
        List<Double> latencyList = new ArrayList<>();

        for (Future<Result> result : results) {
            numberRequests += result.get().getNumberRequests();
            successfulRequest += result.get().getSuccessfulRequests();
            latencyList.addAll(result.get().getLatency());
        }

        statistics.setLatency(latencyList);
        statistics.setNumberRequests(numberRequests);
        statistics.setSuccessfulRequests(successfulRequest);

        System.out.println("Number of GET requests is: " + numberRequests);
        System.out.println("Number of successful GET requests is: " + successfulRequest);
        System.out.println("Time for all GET threads to run takes: " + wallTime + " milliseconds");

        //get mean & median
        Double meanLatencies = latencyList.stream().mapToDouble(l -> l).average().getAsDouble();
        System.out.println("The mean latencies for all GET requests is: " + meanLatencies + " milliseconds");
        Double median = getMedian(latencyList);
        System.out.println("The median latencies for all GET requests is: " + median + " milliseconds");

        //get 99th & 95th percentage
        System.out.println("The 95th percentile GET latency is: " + latencyList.get((int) (latencyList.size() * 0.95)) + " milliseconds");
        System.out.println("The 99th percentile GET latency is: " + latencyList.get((int) (latencyList.size() * 0.99)) + " milliseconds");
    }

    /**
     * Get the median number, given a list
     *
     * @param latencyList a list stores all the latency data
     * @return a double
     */
    private static Double getMedian(List<Double> latencyList) {
        Collections.sort(latencyList);
        if (latencyList.size() % 2 == 0) {
            return (latencyList.get(latencyList.size() / 2 - 1) + latencyList.get(latencyList.size() / 2)) / 2.0;
        }
        return latencyList.get(latencyList.size()) / 2.0;
    }

}
