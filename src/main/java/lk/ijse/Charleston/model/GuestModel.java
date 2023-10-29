package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Guest;
//import lk.ijse.Charleston.dto.tm.GuestTM;
import lk.ijse.Charleston.dto.Tour;
import lk.ijse.Charleston.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestModel {
    public static List<Guest> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Guest";
        List<Guest> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        // ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {
            data.add(new Guest(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return data;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM guest WHERE Guest_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean save(Guest guest) throws SQLException {
        String sql = "INSERT INTO Guest(Guest_ID_Type, Guest_ID, Guest_Name, Guest_Contact_No, Guest_Country, Guest_Email)" +
                "VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, guest.getIdType());
        pstm.setString(2, guest.getId());
        pstm.setString(3, guest.getName());
        pstm.setString(4, guest.getContactNo());
        pstm.setString(5, guest.getCountry());
        pstm.setString(6, guest.getEmail());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> loadIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet resultSet = con.createStatement().executeQuery("SELECT Guest_ID FROM Guest");

        List<String> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }

    public static Guest searchById(String code) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Guest WHERE Guest_ID = ?");
        pstm.setString(1, code);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return new Guest(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }
}