package net.samge.model;


import java.sql.Timestamp;

public class PlaneInfo {

    private long pid;
    private String sStation;
    private String aStation;
    private java.sql.Timestamp sTime;
    private java.sql.Timestamp aTime;
    private long maxCap;
    private String company;
    private long cost;


    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }


    public String getSStation() {
        return sStation;
    }

    public void setSStation(String sStation) {
        this.sStation = sStation;
    }


    public String getAStation() {
        return aStation;
    }

    public void setAStation(String aStation) {
        this.aStation = aStation;
    }


    public java.sql.Timestamp getSTime() {
        return sTime;
    }

    public void setSTime(java.sql.Timestamp sTime) {
        this.sTime = sTime;
    }


    public java.sql.Timestamp getATime() {
        return aTime;
    }

    public void setATime(java.sql.Timestamp aTime) {
        this.aTime = aTime;
    }


    public long getMaxCap() {
        return maxCap;
    }

    public void setMaxCap(long maxCap) {
        this.maxCap = maxCap;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public PlaneInfo(long pid, String sStation, String aStation, Timestamp sTime, Timestamp aTime, long maxCap, String company, long cost) {
        this.pid = pid;
        this.sStation = sStation;
        this.aStation = aStation;
        this.sTime = sTime;
        this.aTime = aTime;
        this.maxCap = maxCap;
        this.company = company;
        this.cost = cost;
    }
}
