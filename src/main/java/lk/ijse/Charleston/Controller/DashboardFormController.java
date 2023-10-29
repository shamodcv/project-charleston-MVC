package lk.ijse.Charleston.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.Charleston.dto.Room;
import lk.ijse.Charleston.dto.tm.RoomTM;
import lk.ijse.Charleston.model.RoomModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardFormController {

    public Label lblTime;
    public Label lblDate;
    public TableView <RoomTM>tblRoom;
    public TableColumn colRoomId;
    public TableColumn colRoomType;
    public TableColumn colRoomDetails;
    public TableColumn colNumberBeds;
    public TableColumn colPrice;
    @FXML
    private AnchorPane root;

    public void initialize() {
        setLblDate();
        setLblTime();
        setValueFactory();
        getAll();
    }
    @FXML
    private void setValueFactory() {
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colRoomDetails.setCellValueFactory(new PropertyValueFactory<>("roomDetails"));
        colNumberBeds.setCellValueFactory(new PropertyValueFactory<>("numberofBeds"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @FXML
    private void getAll() {
        try {
            ObservableList<RoomTM> obList = FXCollections.observableArrayList();
            List<Room> roomList = RoomModel.getAllAvailableRooms();

            for (Room room : roomList) {
                obList.add(new RoomTM(
                        room.getRoomId(),
                        room.getRoomType(),
                        room.getRoomDetails(),
                        room.getNumberofBeds(),
                        room.getPrice()

                ));
            }
            tblRoom.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    @FXML
    public void btnGuestOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/guest_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Guest Management");
        stage.centerOnScreen();

    }
    @FXML
    public void btnReservationOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/reservatioln_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Reservation Management");
        stage.centerOnScreen();
    }
    @FXML
    public void btnReservationsDetailsOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/reservation_details_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Reservation Details");
        stage.centerOnScreen();
    }
    @FXML
    public void btnRoomsOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/room_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Room Management");
        stage.centerOnScreen();
    }
    @FXML
    public void btnFoodMenuOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/foodmenu_form.fxml"));

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("FoodMenu Management");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    @FXML
    public void btnTourOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/tour_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Tour Management");
        stage.centerOnScreen();

    }
    @FXML
    public void btnTourDetailsOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/tour_details_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Tour Details");
        stage.centerOnScreen();
    }
    @FXML
    public void btnPaymentOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/payment_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Payment Management");
        stage.centerOnScreen();
    }
    @FXML
    public void btnOderOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/order_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Orders Management");
        stage.centerOnScreen();

    }
    @FXML
    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
    }
    @FXML
    public void btnStOnAction(MouseEvent mouseEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/admin_form.fxml"));

        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.centerOnScreen();
    }
    @FXML
    public void setLblDate(){
        lblDate.setText(String.valueOf(LocalDate.now()));
    }
    public void setLblTime(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            lblTime.setText(LocalTime.now().format(formatter));
        }),new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
