package net.samge.model;

import java.sql.Date;

public class PriceItem {
    private long pid;
    private String c1;
    private String c2;
    private Date date;
    private long cost;

    public PriceItem(long pid, String c1, String c2, Date date, long cost) {
        this.pid = pid;
        this.c1 = c1;
        this.c2 = c2;
        this.date = date;
        this.cost = cost;
    }

    public long getPid() {
        return pid;
    }

    public String getC1() {
        return c1;
    }

    public String getC2() {
        return c2;
    }

    public Date getDate() {
        return date;
    }



    public long getCost() {
        return cost;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public void setCost(long cost) {
        this.cost = cost;
    }
}
