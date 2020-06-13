package net.samge.model;


import java.sql.Timestamp;

public class Notification {

  private long oid;
  private long received;
  private java.sql.Timestamp notiDate;


  public long getOid() {
    return oid;
  }

  public void setOid(long oid) {
    this.oid = oid;
  }


  public long getReceived() {
    return received;
  }

  public void setReceived(long received) {
    this.received = received;
  }


  public java.sql.Timestamp getNotiDate() {
    return notiDate;
  }

  public void setNotiDate(java.sql.Timestamp notiDate) {
    this.notiDate = notiDate;
  }

  public Notification(long oid, long received, Timestamp notiDate) {
    this.oid = oid;
    this.received = received;
    this.notiDate = notiDate;
  }
}
