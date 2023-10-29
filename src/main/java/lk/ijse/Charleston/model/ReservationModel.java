package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Reservation;
import lk.ijse.Charleston.dto.Tour;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationModel {

    public static String getNextResId() throws SQLException {

            Connection con = DBConnection.getInstance().getConnection();

            String sql = "SELECT Res_ID FROM Resavations ORDER BY Res_ID DESC LIMIT 1";

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
                return "Re00" + "" + id;
            }
            return "Re001";
        }

    public static boolean save(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Resavations(Res_ID, Guest_ID, Room_ID, Room_price,Check_in_Date,Check_out_Date)" +
                "VALUES(?, ?, ?, ?, ?, ? )";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, reservation.getResId());
        pstm.setString(2, reservation.getGusId());
        pstm.setString(3, reservation.getRoomId());
        pstm.setDouble(4, reservation.getRoomPrice());
        pstm.setString(5, reservation.getCheckInDate());
        pstm.setString(6, reservation.getCheckOutDate());

        int affectedRows = pstm.executeUpdate();
        boolean isSave = affectedRows > 0;
        if (isSave){
            String sql2 = "INSERT INTO Resavations_detail(Res_ID, Room_ID, Room_price, Guest_ID, Guest_Name, Check_In_Date, Check_Out_Date)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ? )";

            String sql3 = "SELECT Guest_Name FROM Guest WHERE Guest_ID = ?";

            PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement(sql2);
            PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql3);

            ps.setString(1, reservation.getGusId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            String guestName = rs.getString("Guest_Name");

            pst.setString(1, reservation.getResId());
            pst.setString(2, reservation.getRoomId());
            pst.setString(3, String.valueOf(reservation.getRoomPrice()));
            pst.setString(4, reservation.getGusId());
            pst.setString(5, guestName);
            pst.setString(6, reservation.getCheckInDate());
            pst.setString(7, reservation.getCheckOutDate());

            pst.executeUpdate();

            String release = "Booked";
            String sql4 = "UPDATE Room SET Room_Details = ? WHERE Room_ID = ?";

            PreparedStatement st = DBConnection.getInstance().getConnection().prepareStatement(sql4);
            st.setString(1, release);
            st.setString(2, reservation.getRoomId());
            st.executeUpdate();


        }

        return affectedRows > 0;
    }

    public static List<Reservation> getAll() throws SQLException {

        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Resavations";
        List<Reservation> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            data.add(new Reservation(
                    resultSet.getString("Res_ID"),
                    resultSet.getString("Guest_ID"),
                    resultSet.getString("Room_ID"),
                    resultSet.getDouble("Room_price"),
                    resultSet.getString("Check_in_Date"),
                    resultSet.getString("Check_out_Date")

            ));
        }
        return data;
    }

    public static boolean delete(String resId) throws SQLException {
        String sql = "DELETE FROM Resavations WHERE Res_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, resId);

        return pstm.executeUpdate() > 0;
    }

    public static List<String> loadIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet resultSet = con.createStatement().executeQuery("SELECT Res_ID FROM Resavations");

        List<String> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }

    public static boolean update(Reservation reservation) throws SQLException {
        String sql = "UPDATE Resavations SET Guest_ID = ?, Room_ID = ?, Room_price = ?, Check_in_Date = ?, Check_out_Date = ? WHERE Res_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1,reservation.getGusId() );
        pstm.setString(2,reservation.getRoomId());
        pstm.setDouble(3,reservation.getRoomPrice() );
        pstm.setString(4,reservation.getCheckInDate() );
        pstm.setString(5,reservation.getCheckOutDate() );
        pstm.setString(6,reservation.getResId());


        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;
    }
}
