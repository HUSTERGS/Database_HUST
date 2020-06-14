package net.samge.model;


import java.sql.Timestamp;

public class Notification {

  private long oid;
  private long received;
  private java.sql.Timestamp notiDate;

  private PlaneInfo info;
  private Order order;

  public void setInfo(PlaneInfo info) {
    this.info = info;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public PlaneInfo getInfo() {
    return info;
  }

  public Order getOrder() {
    return order;
  }

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


  @Override
  public String toString() {
    return "您在" + info.getSTime() + "从" + info.getSStation() +
            info.getAStation() + "即将要出发了，请及时进行交款取票操作";
  }
}
