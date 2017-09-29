package com.programmerscuriosity;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private long wallTime;
    private int numberRequests;
    private int successfulRequests;
    private List<Double> latency;


    public Result() {
        wallTime = 0;
        numberRequests = 0;
        successfulRequests = 0;
        latency = new ArrayList<>();
    }
    public Result (long wallTime, int numberRequests, int successfulRequests) {
        this.wallTime = wallTime;
        this.numberRequests = numberRequests;
        this.successfulRequests = successfulRequests;
    }

    public void addNumberRequest() {
        this.setNumberRequests(this.getNumberRequests() + 1);
    }

    public void addSuccessfulRequest() {
        this.setSuccessfulRequests(this.getSuccessfulRequests() + 1);
    }

    public long getWallTime() {
        return wallTime;
    }

    public void setWallTime(long wallTime) {
        this.wallTime = wallTime;
    }

    public int getNumberRequests() {
        return numberRequests;
    }

    public int getSuccessfulRequests() {
        return successfulRequests;
    }

    public void setNumberRequests(int numberRequests) {
        this.numberRequests = numberRequests;
    }

    public void setSuccessfulRequests(int successfulRequests) {
        this.successfulRequests = successfulRequests;
    }

    public List<Double> getLatency() {
        return latency;
    }

    public void setLatency(List<Double> latency) {
        this.latency = latency;
    }

}
