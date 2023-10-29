package lk.ijse.Charleston.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.Food;
import lk.ijse.Charleston.dto.Guest;
import lk.ijse.Charleston.dto.tm.FoodTM;
import lk.ijse.Charleston.model.FoodModel;
import lk.ijse.Charleston.model.GuestModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class FoodMenuFormController {

    @FXML
    private AnchorPane FoodPane;
    @FXML
    private JFXComboBox <String> CmbMealType;
    @FXML
    private TextField txtMealId;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtMealName;
    @FXML
    private TextField txtMealDescription;
    @FXML
    private TextField txtMealPrice;
    @FXML
    private TableColumn colMealType;
    @FXML
    private TableColumn colMealId;
    @FXML
    private TableColumn colMealName;
    @FXML
    private TableColumn colMealDescription;
    @FXML
    private TableColumn  colMealPrice;

    public TableView<FoodTM> tblMeal;

    public void initialize() {
        ObservableList<String> titleList = FXCollections.observableArrayList("Breakfast","Launch","Dinner");
        CmbMealType.setItems(titleList);

        setCellValueFactory();
        setSelectStaffToTxt();
        getAll();
    }
    @FXML
    private void setCellValueFactory() {
        colMealType.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        colMealId.setCellValueFactory(new PropertyValueFactory<>("mealID"));
        colMealName.setCellValueFactory(new PropertyValueFactory<>("mealName"));
        colMealDescription.setCellValueFactory(new PropertyValueFactory<>("mealDescription"));
        colMealPrice.setCellValueFactory(new PropertyValueFactory<>("mealPrice"));
    }
    @FXML
    private void getAll() {
        try {
            ObservableList<FoodTM> obList = FXCollections.observableArrayList();
            List<Food> foodList = FoodModel.getAll();

            for (Food food : foodList) {
                obList.add(new FoodTM(
                        food.getMealType(),
                        food.getMealID(),
                        food.getMealName(),
                        food.getMealDescription(),
                        food.getMealPrice()
                ));
            }
            tblMeal.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    private void setSelectStaffToTxt() {
        tblMeal.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                CmbMealType.setValue(newSelection.getMealType());
                txtMealId.setText(newSelection.getMealID());
                txtMealName.setText(newSelection.getMealName());
                txtMealDescription.setText(newSelection.getMealDescription());
                txtMealPrice.setText(String.valueOf(newSelection.getMealPrice()));

            }
        });
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) FoodPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {


        if(CmbMealType.getValue() != null && !txtMealId.getText().isBlank() && !txtMealName.getText().isBlank() && !txtMealDescription.getText().isBlank() && !txtMealPrice.getText().isBlank()) {
            String mealType = CmbMealType.getValue();
            String mealID = txtMealId.getText();
            String mealName= txtMealName.getText();
            String mealDescription= txtMealDescription.getText();
            double mealPrice = Double.parseDouble(txtMealPrice.getText());

            Food food = new Food(mealType,mealID,mealName,mealDescription,mealPrice);
            try {
                boolean isSaved = FoodModel.save(food);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Meal saved!").show();
                    getAll();
                    setCellValueFactory();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Meal Not saved ,Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }


    public void btnClearOnAction(ActionEvent actionEvent) {
        txtClear();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {

        btnSearchOnAction(actionEvent);
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        tblMeal.getItems().stream()
                .filter(item -> item.getMealID().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblMeal.getSelectionModel().select(item);
                    tblMeal.scrollTo(item);
                    txtSearch.clear();
                });
        tblMeal.getItems().stream()
                .filter(item -> item.getMealName().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblMeal.getSelectionModel().select(item);
                    tblMeal.scrollTo(item);
                    txtSearch.clear();
                });
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

            String mealType = CmbMealType.getValue();
            String mealID = txtMealId.getText();
            String mealName = txtMealName.getText();
            String mealDescription = txtMealDescription.getText();
            double mealPrice = Double.parseDouble(txtMealPrice.getText());

            Food food = new Food(mealType,mealID,mealName,mealDescription,mealPrice);
            try {
                boolean isUpdated = FoodModel.update(food);
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION, "Meal updated!").show();
                    setCellValueFactory();
                    getAll();
                }
                else {
                    new Alert(Alert.AlertType.ERROR, "Meal Id Not Exist!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Meal Id Not Exist!").show();
            }


    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String mealID = txtMealId.getText();
        if(!txtMealId.getText().isBlank()) {
            try {
                boolean isDeleted = FoodModel.delete(mealID);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Meal Deleted!").show();
                    txtClear();
                    getAll();
                    setCellValueFactory();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Meal Id Not Exist!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
               txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Meal Id!").show(); }

    }
    private void txtClear() {
        CmbMealType.getSelectionModel().clearSelection();
        txtMealId.clear();
        txtMealName.clear();
        txtMealDescription.clear();
        txtMealPrice.clear();
    }
}
