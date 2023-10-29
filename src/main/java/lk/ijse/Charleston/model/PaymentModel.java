package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Guest;
import lk.ijse.Charleston.dto.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentModel {
    public static boolean save(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment(PayID , Guest_ID , Guest_Name , Res_ID , Room_ID ,Check_in_Date ,Check_out_Date ,Orders_Ammount , Total_Price )" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, payment.getPaymentId());
        pstm.setString(2, payment.getGuestId());
        pstm.setString(3, payment.getGuestName());
        pstm.setString(4, payment.getResId());
        pstm.setString(5, payment.getRoomId());
        pstm.setString(6, payment.getCheckIn());
        pstm.setString(7, payment.getCheckOut());
        pstm.setDouble(8, payment.getOrderAm());
        pstm.setDouble(9, payment.getTotal());

        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;
    }

    public static boolean update(Payment payment) throws SQLException {
        String sql = "UPDATE Payment SET Guest_ID = ?, Guest_Name = ?, Res_ID = ?, Room_ID = ?, Check_in_Date = ?, Check_out_Date = ?, Orders_Ammount = ?, Total_Price = ? WHERE PayID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, payment.getGuestId());
        pstm.setString(2, payment.getGuestName());
        pstm.setString(3, payment.getResId());
        pstm.setString(4, payment.getRoomId());
        pstm.setString(5, payment.getCheckIn());
        pstm.setString(6, payment.getCheckOut());
        pstm.setDouble(7, payment.getOrderAm());
        pstm.setDouble(8, payment.getTotal());
        pstm.setString(9, payment.getPaymentId());


        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;
    }

    public static boolean delete(String paymentId) throws SQLException {
        String sql = "DELETE FROM Payment WHERE PayID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, paymentId);

        return pstm.executeUpdate() > 0;
    }

    public static List<Payment> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Payment";
        List<Payment> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            data.add(new Payment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getDouble(8),
                    resultSet.getDouble(9)
            ));
        }
        return data;
    }
    public static String getNextId() throws SQLException {

        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT PayID FROM Payment ORDER BY PayID DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        if (resultSet.next()) {
            return splitId(resultSet.getString(1));
        }
        return splitId(null);
    }

    private static String splitId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("00");
            int id = Integer.parseInt(strings[1]);
            id++;
            return "PA00" + "" + id;
        }
        return "PA001";
    }
}
