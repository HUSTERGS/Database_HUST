package net.samge.dbController;

import javafx.collections.ArrayChangeListener;
import net.samge.db.DBConnection;
import net.samge.model.*;
import net.samge.view.controller.UserOrderItem;

import java.sql.*;
import java.util.ArrayList;

public class UserController {
    public static User UserLogin(String email, String password) {
        String SQL = "select * from Users where Email=? and Password=?";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setObject(1, email);
            stm.setObject(2,password);
            ResultSet rst = stm.executeQuery();
            if (rst.next()) {
                return new User(
                        rst.getLong(1),
                        rst.getLong(2),
                        rst.getString(3),
                        rst.getString(4),
                        rst.getString(5),
                        rst.getString(6),
                        rst.getString(7)
                );
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<UserOrder> getAllOrdersOfUser(User user) {
        String SQL = "select PlaneInfo.*, `Order`.Canceled, `Order`.Oid\n" +
                "from `Order`, `PlaneInfo`\n" +
                "where `Order`.Pid = PlaneInfo.Pid and\n" +
                "      `Order`.Uid = " + Long.toString(user.getUid());
        ArrayList<UserOrder> items = new ArrayList<>();
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            while (rst.next()) {
                PlaneInfo i =  new PlaneInfo(
                        rst.getLong(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getTimestamp(4),
                        rst.getTimestamp(5),
                        rst.getLong(6),
                        rst.getString(7),
                        rst.getLong(8)
                );

                Order o = new Order(
                        rst.getLong(10),
                        user.getUid(),
                        rst.getLong(1),
                        rst.getLong(9)
                );
                items.add(new UserOrder(i, user, o));
            }
            return items;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void cancelOrder(Order order) {
        String SQL = "update `Order` set Canceled=true where Oid = " + order.getOid();
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            stm.executeUpdate(SQL);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ArrayList<Notification> getAllNotifications(User user) {
        // 获得某一个用户所有的通知信息
        String SQL = "select `Notification`.* from `Notification`, `Order` where `Order`.Oid =Notification.Oid and " +
                "                                            `Order`.Uid = " + user.getUid();
        ArrayList<Notification> result = new ArrayList<>();
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            while (rst.next()) {
                result.add(new Notification(
                        rst.getLong(1),
                        rst.getLong(2),
                        rst.getTimestamp(3)
                ));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public static User UserRegister(String email, String password) {
        if (UserLogin(email, password) != null) {
            return null;
        }
        String SQL = "insert into Users (isAdmin, Email, Password, Username, IDCardNum, PhoneNum) VALUES (false, ?, ? ,'default', '', '')";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setObject(1, email);
            stm.setObject(2,password);
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return UserLogin(email, password);
    }

    public static User getUser(long uid) {
        String SQL = "select * from Users where Uid=?";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setObject(1, uid);
            ResultSet rst = stm.executeQuery();
            if (rst.next()) {
                return new User(
                        rst.getLong(1),
                        rst.getLong(2),
                        rst.getString(3),
                        rst.getString(4),
                        rst.getString(5),
                        rst.getString(6),
                        rst.getString(7)
                );
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateUserInfo(User user) {
        StringBuilder builder = new StringBuilder("update Users ");
        boolean flag = false;
        // 如果什么都没有改变
        if (user.getPassword().isEmpty() &&
        user.getPhoneNum().isEmpty() &&
        user.getIdCardNum().isEmpty() &&
        user.getEmail().isEmpty() &&
        user.getUsername().isEmpty()) {
            return true;
        }
        if (!user.getUsername().isEmpty()) {
            builder.append(" set Username=").append("'").append(user.getUsername()).append("'");
            flag = true;
        }
        if (!user.getEmail().isEmpty()) {
            if (!flag) {
                builder.append(" set Email=").append("'").append(user.getEmail()).append("'");
                flag = true;
            }
            else {
                builder.append(", Email=").append("'").append(user.getEmail()).append("'");
            }
        }
        if (!user.getIdCardNum().isEmpty()) {
            if (!flag) {
                builder.append(" set IDCardNum=").append("'").append(user.getIdCardNum()).append("'");
                flag = true;
            }
            else {
                builder.append(", IDCardNum=").append("'").append(user.getIdCardNum()).append("'");
            }
        }
        if (!user.getPhoneNum().isEmpty()) {
            if (!flag) {
                builder.append(" set PhoneNum=").append("'").append(user.getPhoneNum()).append("'");
                flag = true;
            } else {
                builder.append(", PhoneNum=").append("'").append(user.getPhoneNum()).append("'");
            }
        }
        if (!user.getPassword().isEmpty()) {
            if (!flag) {
                builder.append(" set Password=").append("'").append(user.getPassword()).append("'");
                flag = true;
            } else {
                builder.append(", Password=").append("'").append(user.getPassword()).append("'");
            }
        }
        builder.append(" where Uid=").append(user.getUid());
        System.out.println(builder.toString());
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            stm.executeUpdate(builder.toString());
            return true;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    public static boolean isUserReady(long uid) {
        User user = getUser(uid);
        return !user.getPhoneNum().isEmpty() &&
                !user.getIdCardNum().isEmpty();
    }
}
    