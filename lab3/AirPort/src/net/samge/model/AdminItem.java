package net.samge.model;

import java.sql.Date;

public class AdminItem {
    private long pid;
    private String c1;
    private String c2;
    private Date date;
    private String orderRate;
    private String seatRate;

    public AdminItem(long pid, String c1, String c2, Date date, String orderRate, String seatRate) {
        this.pid = pid;
        this.c1 = c1;
        this.c2 = c2;
        this.date = date;
        this.orderRate = orderRate;
        this.seatRate = seatRate;
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

    public String getOrderRate() {
        return orderRate;
    }

    public String getSeatRate() {
        return seatRate;
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

    public void setOrderRate(String orderRate) {
        this.orderRate = orderRate;
    }

    public void setSeatRate(String seatRate) {
        this.seatRate = seatRate;
    }

    @Override
    public String toString() {
        return "AdminItem{" +
                "pid=" + pid +
                ", c1='" + c1 + '\'' +
                ", c2='" + c2 + '\'' +
                ", date=" + date +
                ", orderRate='" + orderRate + '\'' +
                ", seatRate='" + seatRate + '\'' +
                '}';
    }
}

