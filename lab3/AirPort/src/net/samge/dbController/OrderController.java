package net.samge.dbController;

import net.samge.db.DBConnection;
import net.samge.model.Order;
import net.samge.model.PriceItem;

import java.sql.*;
import java.util.ArrayList;

public class OrderController {
    public static boolean order(long uid, long pid) {
        String SQL = "insert into `Order` (Uid, Pid, Canceled) VALUES (?, ?, false)";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setObject(1, uid);
            stm.setObject(2, pid);
            stm.execute();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Order getOrder(long oid) {
        String SQL = "select * from `Order` where Oid=" + Long.toString(oid);
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            if (rst.next()) {
                return new Order(
                        rst.getLong(1),
                        rst.getLong(2),
                        rst.getLong(3),
                        rst.getLong(4)
                );
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 查看是否已经订购
     * @param oid
     * @param uid
     * @return
     */
    public static boolean getOrderOfUser(long pid, long uid) {
        String SQL = "select * from `Order` where Canceled=false and Pid=" + pid + " and Uid=" + uid;
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            return rst.next();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ArrayList<PriceItem> getAllBillingRecord(long pid) {
        ArrayList<PriceItem> result = new ArrayList<>();
        String SQL = "select PlaneInfo.Pid, PlaneInfo.SStation, PlaneInfo.AStation, date(PlaneInfo.STime), PlaneInfo.Cost from PlaneInfo, `Order`, Users, Notification\n" +
                "where PlaneInfo.Pid = `Order`.Pid and\n" +
                "      `Order`.Uid = Users.Uid and\n" +
                "      `Order`.Canceled = false and\n" +
                "      Notification.Received = true and\n" +
                "      Notification.Oid = `Order`.Oid and\n" +
                "      Users.Uid = " + pid;
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            while (rst.next()) {
                result.add(
                        new PriceItem(
                                rst.getLong(1),
                                rst.getString(2),
                                rst.getString(3),
                                rst.getDate(4),
                                rst.getLong(5)
                        )
                );
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
