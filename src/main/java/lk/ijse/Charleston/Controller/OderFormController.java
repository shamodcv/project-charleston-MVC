package lk.ijse.Charleston.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Charleston.dto.Food;
import lk.ijse.Charleston.dto.OrderDetails;
import lk.ijse.Charleston.dto.ReservationDetails;
import lk.ijse.Charleston.dto.tm.OrderTM;
import lk.ijse.Charleston.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OderFormController implements Initializable {

    @FXML
    private AnchorPane OderPane;
    @FXML
    private TextField txtSearch;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private Label lblGuestId;
    @FXML
    private Label lblRoomId;
    @FXML
    private Label lblGuestName;
    @FXML
    private Label lblMealName;
    @FXML
    private Label lblMealPrice;
    @FXML
    private TextField txtQty;

    public JFXButton btnPlaceOrder;

    public TableView <OrderTM>tblOder;
    @FXML
    private TableColumn colOrderId;
    @FXML
    private TableColumn colReservation;
    @FXML
    private TableColumn colRoomId;
    @FXML
    private TableColumn colGuestId;
    @FXML
    private TableColumn colGuestName;
    @FXML
    private TableColumn colMealId;
    @FXML
    private TableColumn colMealName;
    @FXML
    private TableColumn colMealPrice;
    @FXML
    private TableColumn colQty;
    @FXML
    private TableColumn colTotalPrice;
    @FXML
    private TableColumn  colOrderDate;
    @FXML
    private TableColumn colOption;
    @FXML
    private Label lblOrderDate;
    @FXML
    private TextField txtOrderId;
    @FXML
    private JFXComboBox <String>cmbResId;
    @FXML
    private JFXComboBox <String>cmbMealId;
    private ObservableList<OrderTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    loadResIds();
    loadMealIds();
    setOrderDate();
    setCellValueFactory();
    generateNextId();

    }

    void setCellValueFactory() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colReservation.setCellValueFactory(new PropertyValueFactory<>("resID"));
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        colGuestId.setCellValueFactory(new PropertyValueFactory<>("guestID"));
        colGuestName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colMealId.setCellValueFactory(new PropertyValueFactory<>("mealID"));
        colMealName.setCellValueFactory(new PropertyValueFactory<>("mealName"));
        colMealPrice.setCellValueFactory(new PropertyValueFactory<>("mealPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) OderPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String oId = txtOrderId.getText();
        String cusId = cmbResId.getValue();

        List<OrderDetails> cartDTOList = new ArrayList<>();

        for (int i = 0; i < tblOder.getItems().size(); i++) {
            OrderTM orderTM = obList.get(i);

            OrderDetails dto = new OrderDetails(
                    orderTM.getOrderID(),
                    orderTM.getMealID(),
                    orderTM.getQty(),
                    orderTM.getTotal()
            );
            cartDTOList.add(dto);
        }
        String date = lblOrderDate.getText();
        boolean isPlaced = false;
        try {
            isPlaced = OrderModel.placeOrder(cartDTOList,oId,cusId);
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed").show();
                generateNextId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order Not Placed").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error").show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
    }

    @FXML
    void btnAddCartOnAction(ActionEvent actionEvent) {

        String code = cmbMealId.getValue();
        String description = lblMealName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblMealPrice.getText());
        double total = qty * unitPrice;
        Button removeBtn = new Button("Remove");
        setRemoveBtnOnAction(removeBtn);



        if (!obList.isEmpty()) {
            for (int i = 0; i < tblOder.getItems().size(); i++) {
                if (colMealId.getCellData(i).equals(code)) {
                    qty += (int) colQty.getCellData(i);
                    total = qty * unitPrice;

                    obList.get(i).setQty(qty);
                    obList.get(i).setTotal(total);

                    tblOder.refresh();
                    calculateNetTotal();
                    return;
                }
            }
        }

        OrderTM tm = new OrderTM(txtOrderId.getText(), cmbResId.getValue(), lblRoomId.getText(),lblGuestId.getText(), colGuestName.getText(), lblOrderDate.getText(),code,description,unitPrice,qty,total,removeBtn);

        obList.add(tm);
        tblOder.setItems(obList);
        calculateNetTotal();

        txtQty.setText("");

    }

    private void calculateNetTotal() {
        double netTotal = 0.0;
        for (int i = 0; i < tblOder.getItems().size(); i++) {
            double total = (double) colTotalPrice.getCellData(i);
            netTotal += total;
        }
    }
    private void setRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                int index=tblOder.getItems().size()-1;
                obList.remove(index);

                tblOder.refresh();
                calculateNetTotal();
            }
         });
    }

    private void loadResIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> ids = ReservationModel.loadIds();

            for (String id : ids) {
                obList.add(id);
            }
            cmbResId.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    private void loadMealIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> ids = FoodModel.loadIds();

            for (String id : ids) {
                obList.add(id);
            }
            cmbMealId.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    private void setOrderDate() {
        lblOrderDate.setText(String.valueOf(LocalDate.now()));
    }
    
    @FXML
    public void ResOnAction(ActionEvent actionEvent) {
        String code = cmbResId.getValue();
        try {
            ReservationDetails res = ReservationDetailsModel.searchById(code);
            fillItemFields(res);

            txtQty.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void fillItemFields(ReservationDetails res) {
        lblGuestId.setText(res.getGuest_ID());
        lblRoomId.setText(String.valueOf(res.getRoom_ID()));
        lblGuestName.setText(String.valueOf(res.getGuest_Name()));
    }

    public void mealOnAction(ActionEvent actionEvent) {
        String code = cmbMealId.getValue();
        try {
            Food food = FoodModel.searchById(code);
            fillMealFields(food);

            txtQty.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void fillMealFields(Food food) {
        lblMealName.setText(food.getMealName());
        lblMealPrice.setText(String.valueOf(food.getMealPrice()));
    }
    private void generateNextId() {
        try {
            String id = PlaceOrderModel.getNextId();
            txtOrderId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
}
