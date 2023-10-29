package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.TourDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourDetailModel {

    public static boolean save(TourDetails tourDetails) throws SQLException {
        String sql = "INSERT INTO Tour_Details(Book_ID,Guest_ID,Guest_Name,Tour_ID,Tour_Name,Price)" +
                "VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, tourDetails.getBookID());
        pstm.setString(2, tourDetails.getGuestID());
        pstm.setString(3, tourDetails.getGuestName());
        pstm.setString(4, tourDetails.getTourID());
        pstm.setString(5, tourDetails.getTourName());
        pstm.setDouble(6, tourDetails.getPrice());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(TourDetails tourDetails) throws SQLException {
        String sql = "UPDATE Tour_Details SET Guest_ID = ?, Guest_Name = ?, Tour_ID = ?, Tour_Name = ?, Price = ? WHERE Book_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, tourDetails.getGuestID());
        pstm.setString(2, tourDetails.getGuestName());
        pstm.setString(3, tourDetails.getTourID());
        pstm.setString(4, tourDetails.getTourName());
        pstm.setDouble(5, tourDetails.getPrice());
        pstm.setString(6, tourDetails.getBookID());

        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;
    }

    public static boolean delete(String bookid) throws SQLException {
        String sql = "DELETE FROM Tour_Details WHERE Book_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, bookid);

        return pstm.executeUpdate() > 0;
    }

    public static List<TourDetails> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Tour_Details";
        List<TourDetails> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            data.add(new TourDetails(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getDouble(6)
            ));
        }
        return data;
    }
    public static String getNextResId() throws SQLException {

        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT Book_ID FROM Tour_Details ORDER BY Book_ID DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        if (resultSet.next()) {
            return splitResId(resultSet.getString(1));
        }
        return splitResId(null);
    }

    private static String splitResId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("00");
            int id = Integer.parseInt(strings[1]);
            id++;
            return "B00" + "" + id;
        }
        return "B001";
    }
}
