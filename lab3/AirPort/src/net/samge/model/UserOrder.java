package net.samge.model;

public class UserOrder {
    public PlaneInfo info;
    public User user;
    public Order order;

    public UserOrder(PlaneInfo info, User user, Order order) {
        this.info = info;
        this.user = user;
        this.order = order;
    }


}
