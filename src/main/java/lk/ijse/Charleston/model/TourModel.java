package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.ReservationDetails;
import lk.ijse.Charleston.dto.Room;
import lk.ijse.Charleston.dto.Tour;
import lk.ijse.Charleston.dto.TourDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TourModel {
    public static List<Tour> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Tour";
        List<Tour> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        // ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {
            data.add(new Tour(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)

            ));
        }
        return data;
    }

    public static boolean delete(String tourId) throws SQLException {
        String sql = "DELETE FROM Tour WHERE Tour_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, tourId);

        return pstm.executeUpdate() > 0;
    }
    public static boolean update(Tour tour) throws SQLException {
        String sql = "UPDATE Tour SET Tour_Name = ?, Tour_Description = ?, Price = ? WHERE Tour_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, tour.getTourName());
        pstm.setString(2, tour.getTourDescription());
        pstm.setDouble(3, tour.getTourPrice());
        pstm.setString(4, tour.getTourId());


        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;

    }

    public static boolean save(Tour tour) throws SQLException {
        String sql = "INSERT INTO Tour(Tour_ID, Tour_Name, Tour_Description, Price)" +
                "VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, tour.getTourId());
        pstm.setString(2, tour.getTourName());
        pstm.setString(3, tour.getTourDescription());
        pstm.setDouble(4, tour.getTourPrice());
        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;
    }

    public static List<String> loadIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet resultSet = con.createStatement().executeQuery("SELECT Tour_ID FROM Tour");

        List<String> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }

    public static Tour searchById(String code) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Tour WHERE Tour_ID = ?");
        pstm.setString(1, code);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return new Tour(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)


            );
        }
        return null;
    }
}
