package net.samge.dbController;

import net.samge.db.DBConnection;
import net.samge.model.Notification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NotificationController {
    public static void noticeUser(Notification notice) {
        String SQL = "update Notification set Received=true where Oid = " + notice.getOid();
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            stm.executeUpdate(SQL);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
