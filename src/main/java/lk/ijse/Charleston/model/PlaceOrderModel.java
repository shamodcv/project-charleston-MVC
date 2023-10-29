package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.OrderDetails;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderModel {
    public static boolean save(String oId, LocalDate now, String resId) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "INSERT INTO Orders(orderId, date, Res_ID) VALUES(?, ?, ?)";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, oId);
        pstm.setDate(2, Date.valueOf(now));
        pstm.setString(3, resId);

        return pstm.executeUpdate() > 0;
    }

    public static boolean saveOrderDetails(List<OrderDetails> cartDTOList) throws SQLException {
        for(OrderDetails orderDetails : cartDTOList) {
            if(!updateQty(orderDetails)) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(OrderDetails orderDetails) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "INSERT INTO OrderDetail(orderId, Meal_No , qty ,Ammount ) VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, orderDetails.getOrderID());
        pstm.setString(2, orderDetails.getMealID());
        pstm.setInt(3, orderDetails.getQty());
        pstm.setDouble(4, orderDetails.getTotal());

        return pstm.executeUpdate() > 0;
    }

    public static double getOrderAm(String resId) throws SQLException {

        String sql = "SELECT orderId FROM Orders WHERE Res_ID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, resId);
        ResultSet rs = pstm.executeQuery();


        ArrayList<String> orderIds = new ArrayList<>();
        while (rs.next()) {
            String orderId = rs.getString("orderId");
            orderIds.add(orderId);
        }

        double totalAmount = 0.0;
        String sql2 = "SELECT Ammount FROM OrderDetail WHERE orderId = ?";
        PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement(sql2);
        for (String orderId : orderIds) {
            pst.setString(1, orderId);
            ResultSet rst = pst.executeQuery();
            double orderAmount = 0.0;
            while (rst.next()) {
                orderAmount += rst.getDouble("Ammount");
            }

            totalAmount += orderAmount;
        }
        return totalAmount;
    }
    public static String getNextId() throws SQLException {

        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT orderId FROM OrderDetail ORDER BY orderId DESC LIMIT 1";

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
            return "OR00" + "" + id;
        }
        return "OR001";
    }
}
