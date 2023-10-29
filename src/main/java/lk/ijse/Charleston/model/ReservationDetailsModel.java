package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.ReservationDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDetailsModel {
    public static List<ReservationDetails> getAll() throws SQLException {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM Resavations_detail";

            List<ReservationDetails> data = new ArrayList<>();

            ResultSet resultSet = con.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                data.add(new ReservationDetails(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                ));
            }
            return data;

    }

    public static ReservationDetails searchById(String code) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Resavations_detail WHERE Res_ID = ?");
        pstm.setString(1, code);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return new ReservationDetails(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)

            );
        }
        return null;
    }
}
