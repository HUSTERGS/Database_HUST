package net.samge.dbController;

import net.samge.db.DBConnection;
import net.samge.model.PlaneInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaneInfoController {
    static public ArrayList<PlaneInfo> getPlaneInfos(String date, String leaveCity, String targetCity) {
        ArrayList<PlaneInfo> result = new ArrayList<>();
        String SQL = "select * fromm PlaneInfo where SStation=? and AStation=? and date(STime) =? order by ATime";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setObject(1, leaveCity);
            stm.setObject(2,targetCity);
            stm.setObject(3, date);
            ResultSet rst = stm.executeQuery();
            while (rst.next()) {
                result.add(new PlaneInfo(
                        rst.getString(1),
                        rst.getString(2),
                        rst.getTimestamp(3),
                        rst.getTimestamp(4),
                        rst.getString(5),
                        rst.getLong(6)
                ));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
