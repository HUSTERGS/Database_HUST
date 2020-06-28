package net.samge.dbController;

import jdk.nashorn.internal.ir.LiteralNode;
import net.samge.db.DBConnection;
import net.samge.model.AdminItem;
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


    static public ArrayList<PlaneInfo> getAllPlaneInfos() {
        ArrayList<PlaneInfo> result = new ArrayList<>();
        String SQL = "select * from PlaneInfo";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stm = conn.prepareStatement(SQL);
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

    public static ArrayList<AdminItem> getAllInfo() {
        ArrayList<AdminItem> result = new ArrayList<>();
        ArrayList<Long> pids = new ArrayList<>();
        //满座率
        String SQL1 = "select PlaneInfo.Pid, PlaneInfo.SStation, PlaneInfo.AStation, date(PlaneInfo.STime) `SDate`, PlaneInfo.MaxCap, count(*) `Count`\n" +
                "from Notification,\n" +
                "     PlaneInfo,\n" +
                "     `Order`\n" +
                "where PlaneInfo.Pid = `Order`.Pid\n" +
                "  and PlaneInfo.STime < now()\n" +
                "  and Notification.Oid = `Order`.Oid\n" +
                "  and Notification.Received = true\n" +
                "group by PlaneInfo.Pid;";
        // 预定情况
        String SQL2 = "select PlaneInfo.Pid, PlaneInfo.SStation, PlaneInfo.AStation, date(PlaneInfo.STime) `SDate`, PlaneInfo.MaxCap, count(*) `Count`\n" +
                "from  PlaneInfo,\n" +
                "     `Order`\n" +
                "where PlaneInfo.Pid = `Order`.Pid\n" +
                "  and PlaneInfo.STime > now()\n" +
                "  and `Order`.Canceled = false\n" +
                "group by PlaneInfo.Pid;";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(SQL1);

            while (rst.next()) {
                pids.add(rst.getLong(1));
                result.add(new AdminItem(
                        rst.getLong(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getDate(4),
                        "-",
                        Double.toString(rst.getLong(6) * 100.0 / rst.getLong(5)) + "%"
                ));
            }
            // 预定率
            rst = stm.executeQuery(SQL2);
            while (rst.next()) {
                pids.add(rst.getLong(1));
                result.add(new AdminItem(
                        rst.getLong(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getDate(4),
                        Double.toString(rst.getLong(6) * 100.0 / rst.getLong(5)) + "%",
                        "-"
                ));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        for (PlaneInfo info : PlaneInfoController.getAllPlaneInfos()) {
            if (!pids.contains(info.getPid())) {
                // 对于被筛选掉的的数据
                if (info.getATime().before(new Timestamp(System.currentTimeMillis()))) {
                    // 如果是在之前，也就是已经结束了
                    result.add(
                            new AdminItem(
                                    info.getPid(),
                                    info.getSStation(),
                                    info.getSStation(),
                                    new Date(info.getSTime().getTime()),
                                    "-",
                                    "0%"
                            )
                    );
                } else {
                    result.add(
                            new AdminItem(
                                    info.getPid(),
                                    info.getSStation(),
                                    info.getSStation(),
                                    new Date(info.getSTime().getTime()),
                                    "0%",
                                    "-"
                            )
                    );
                }
            }
        }
        return result;
    }


}
