package com.programmerscuriosity.model;

import java.io.Serializable;

public class SkierData implements Serializable, Comparable<SkierData>{
    private int skierID;
    private int totalLifts;
    private int totalVert;
    private int dayNum;

    public SkierData(int skierID, int totalLifts, int totalVert, int dayNum) {
        this.skierID = skierID;
        this.totalLifts = totalLifts;
        this.totalVert = totalVert;
        this.dayNum = dayNum;
    }
    
    public SkierData(int skierID, int dayNum) {
        this.skierID = skierID;
        this.dayNum = dayNum;
    }

    public SkierData() {
    }

       
    public int getSkierID() {
        return skierID;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    public int getTotalLifts() {
        return totalLifts;
    }

    public void setTotalLifts(int totalLifts) {
        this.totalLifts = totalLifts;
    }

    public int getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(int totalVert) {
        this.totalVert = totalVert;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }
    
    @Override
    public int compareTo(SkierData compareData) {
        int compareID = ((SkierData) compareData).getSkierID();
        //ascending order
        return this.skierID - compareID;
    }
    
    public String toSQLString() {
        return ("(" + skierID + "," + totalLifts + "," + totalVert + "," + dayNum + ")");
    }
    
}
