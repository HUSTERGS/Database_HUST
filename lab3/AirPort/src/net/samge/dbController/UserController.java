package net.samge.dbController;

import net.samge.db.DBConnection;
import net.samge.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public static User UserLogin(String email, String password) {
        String SQL = "select * from database_lab3.User where Email=? and Password=?";
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
                        rst.getString(5)
                );
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
