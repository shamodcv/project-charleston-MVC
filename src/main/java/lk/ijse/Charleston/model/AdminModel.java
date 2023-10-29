package lk.ijse.Charleston.model;

import javafx.scene.control.Alert;
import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminModel {
    public static boolean check(Admin admin) throws SQLException {
        String sql = "SELECT * FROM Admin";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            String username = resultSet.getString("UserName");
            String password = resultSet.getString("Password");

            if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
                return true;
            }if (!username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
                new Alert(Alert.AlertType.ERROR, "Username Invaild!").show();
            }if (username.equals(admin.getUsername()) && !password.equals(admin.getPassword())) {
                new Alert(Alert.AlertType.ERROR, "Password Invaild!").show();
            }if (!username.equals(admin.getUsername()) && !password.equals(admin.getPassword())) {
                new Alert(Alert.AlertType.ERROR, "Username & Password Invaild!").show();
            }
        }

        return false;
    }

    public static boolean adminUpdate(Admin admin, String olduser) throws SQLException {
        int affectedRows = 0;

        String sql = "SELECT * FROM Admin WHERE LOWER(Password) = LOWER(?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, olduser);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) { // Check if result set has any rows
            String oldusername = rs.getString("Password");
            if (olduser.equals(oldusername)) {

                String sql2 = "UPDATE Admin SET UserName = ?, Password = ? WHERE Password = ?";
                PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement(sql2);

                pst.setString(1, admin.getUsername());
                pst.setString(2, admin.getPassword());
                pst.setString(3, olduser);

                affectedRows = pst.executeUpdate();


            }
        }
        return affectedRows > 0;
    }
}
