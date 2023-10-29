package lk.ijse.Charleston.model;

import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Food;
import lk.ijse.Charleston.dto.Guest;
import lk.ijse.Charleston.dto.ReservationDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodModel {
    public static List<Food> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Food_Menu";
        List<Food> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            data.add(new Food(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            ));
        }
        return data;
    }
    public static boolean delete(String mealID) throws SQLException {
        String sql = "DELETE FROM food_menu WHERE Meal_No=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, mealID);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Food food) throws SQLException {
        String sql = "UPDATE Food_Menu SET Meal_Type = ?, Meal_Name = ?, Meal_Discription = ?, Meal_Price = ? WHERE Meal_No = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, food.getMealType());
        pstm.setString(2, food.getMealName());
        pstm.setString(3, food.getMealDescription());
        pstm.setDouble(4, food.getMealPrice());
        pstm.setString(5, food.getMealID());


        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;

    }

    public static boolean save(Food food) throws SQLException {
        String sql = "INSERT INTO Food_Menu(Meal_Type, Meal_No, Meal_Name, Meal_Discription, Meal_Price)"+
                "VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,food.getMealType());
        pstm.setString(2, food.getMealID());
        pstm.setString(3, food.getMealName());
        pstm.setString(4, food.getMealDescription());
        pstm.setDouble(5, food.getMealPrice());
        int affectedRows = pstm.executeUpdate();

        return affectedRows > 0;
    }

    public static List<String> loadIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet resultSet = con.createStatement().executeQuery("SELECT Meal_No FROM Food_Menu");

        List<String> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }

    public static Food searchById(String code) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Food_Menu WHERE Meal_No = ?");
        pstm.setString(1, code);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return new Food(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)

            );
        }
        return null;
    }
}
