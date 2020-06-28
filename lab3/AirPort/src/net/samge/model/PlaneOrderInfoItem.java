package net.samge.model;

public class PlaneOrderInfoItem {
    String UserName;
    String ID;
    String seatNo;
    long payed;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public PlaneOrderInfoItem(String userName, String ID, String seatNo, long payed) {
        UserName = userName;
        this.ID = ID;
        this.seatNo = seatNo;
        this.payed = payed;
    }

    public long getPayed() {
        return payed;
    }

    public void setPayed(long payed) {
        this.payed = payed;
    }
}
