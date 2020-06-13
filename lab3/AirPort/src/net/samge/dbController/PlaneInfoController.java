package net.samge.dbController;

import jdk.nashorn.internal.ir.LiteralNode;
import net.samge.db.DBConnection;
import net.samge.model.PlaneInfo;

import java.sql.*;
import java.util.ArrayList;

public class PlaneInfoController {
    static public ArrayList<PlaneInfo> getPlaneInfos(String date, String leaveCity, String targetCity) {
        ArrayList<PlaneInfo> result = new ArrayList<>();
        String SQL = "select * from PlaneInfo where SStation=? and AStation=? and date(STime) =? order by ATime";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setObject(1, leaveCity);
            stm.setObject(2,targetCity);
            stm.setObject(3, date);
            ResultSet rst = stm.executeQuery();
            while (rst.next()) {
                result.add(new PlaneInfo(
                        rst.getLong(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getTimestamp(4),
                        rst.getTimestamp(5),
                        rst.getLong(6),
                        rst.getString(7),
                        rst.getLong(8)
                ));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getAllLeavingCities() {
        String SQL = "select distinct SStation from PlaneInfo";
        ArrayList<String> result = new ArrayList<>();
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            while (rst.next()) {
                result.add(rst.getString(1));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getAllArrivingCities() {
        String SQL = "select distinct AStation from PlaneInfo";
        ArrayList<String> result = new ArrayList<>();
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            while (rst.next()) {
                result.add(rst.getString(1));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isPlaneFull(PlaneInfo info) {
        // 判断某一个航班的人数是否打到到最大值
        return info.getMaxCap() <= getOrderedNum(info.getPid());
    }

    public static int getOrderedNum(long pid) {
        // 返回某一个航班预定的未退票的所有人
        String SQL = "select count(*) from `Order` where Canceled=false and Pid=" + Long.toString(pid);
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            if (rst.next()) {
                return rst.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static PlaneInfo getPlaneInfo(long pid) {
        String SQL = "select * from PlaneInfo where Pid=" + Long.toString(pid);
        System.out.println(SQL);
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            if (rst.next()) {
                return new PlaneInfo(
                        rst.getLong(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getTimestamp(4),
                        rst.getTimestamp(5),
                        rst.getLong(6),
                        rst.getString(7),
                        rst.getLong(8)
                );
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
