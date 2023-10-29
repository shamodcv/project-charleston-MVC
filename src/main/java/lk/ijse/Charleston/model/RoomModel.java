package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Food;
import lk.ijse.Charleston.dto.Guest;
import lk.ijse.Charleston.dto.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomModel {
    public static List<Room> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM room";
        List<Room> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        // ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {
            data.add(new Room(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            ));
        }
        return data;
    }
    public static boolean delete(String roomId) throws SQLException {
        String sql = "DELETE FROM room WHERE Room_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, roomId);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Room room) throws SQLException {
        String sql = "UPDATE room SET Room_Type = ?, Room_Details = ?, Number_Of_Beds = ?, Room_price = ? WHERE Room_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, room.getRoomType());
        pstm.setString(2, room.getRoomDetails());
        pstm.setString(3, room.getNumberofBeds());
        pstm.setDouble(4, room.getPrice());
        pstm.setString(5, room.getRoomId());


        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;

    }

    public static boolean save(Room room) throws SQLException {
        String sql = "INSERT INTO room(Room_ID, Room_Type, Room_Details, Number_Of_Beds, Room_price)" +
                "VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,room.getRoomId());
        pstm.setString(2, room.getRoomType());
        pstm.setString(3, room.getRoomDetails());
        pstm.setString(4, room.getNumberofBeds());
        pstm.setDouble(5, room.getPrice());
        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;

    }

    public static List<String> loadIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet resultSet = con.createStatement().executeQuery("SELECT Room_ID FROM Room WHERE Room_Details = 'Available'");

        List<String> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;

    }

    public static void releaseRoom(String roomId, String release) throws SQLException {
        String sql = "UPDATE Room SET Room_Details = ? WHERE Room_ID = ?";

        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, release);
        pstm.setString(2, roomId);
        pstm.executeUpdate();

    }
    public static Room searchById(String code) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Room WHERE Room_ID = ?");
        pstm.setString(1, code);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return new Room(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            );
        }
        return null;
    }
    public static List<Room> getAllAvailableRooms() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Room WHERE Room_Details = 'Available'";
        List<Room> availableRooms = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            availableRooms.add(new Room(
                    resultSet.getString("Room_ID"),
                    resultSet.getString("Room_Type"),
                    resultSet.getString("Room_Details"),
                    resultSet.getString("Number_OF_Beds"),
                    resultSet.getDouble("Room_price")
            ));
        }
        return availableRooms;
    }


}
