package net.samge.dbController;

import net.samge.db.DBConnection;
import net.samge.model.Order;

import java.sql.*;

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
}
