package net.samge.model;


public class Order {

    private long oid;
    private long uid;
    private long pid;
    private long canceled;


    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }


    public long getCanceled() {
        return canceled;
    }

    public void setCanceled(long canceled) {
        this.canceled = canceled;
    }

    public Order(long oid, long uid, long pid, long canceled) {
        this.oid = oid;
        this.uid = uid;
        this.pid = pid;
        this.canceled = canceled;
    }
}
