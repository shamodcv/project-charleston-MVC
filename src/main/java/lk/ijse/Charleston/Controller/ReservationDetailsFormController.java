package lk.ijse.Charleston.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Charleston.dto.Reservation;
import lk.ijse.Charleston.dto.ReservationDetails;
import lk.ijse.Charleston.dto.tm.ReservationDetailsTM;
import lk.ijse.Charleston.dto.tm.ReservationTM;
import lk.ijse.Charleston.model.ReservationDetailsModel;
import lk.ijse.Charleston.model.ReservationModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationDetailsFormController implements Initializable {
   @FXML
   private AnchorPane ReservationDetailsPane;
   @FXML
   private TextField txtSearch;
   public TableView <ReservationDetailsTM> tblReservationDetails;
   @FXML
   private TableColumn colReservationId;
   @FXML
   private TableColumn colRoomId;
   @FXML
   private TableColumn colCheckinDate;
   @FXML
   private TableColumn colCheckoutDate;
   @FXML
   private TableColumn colGuestId;
   @FXML
   private TableColumn colGuestName;
   @FXML
   private TableColumn colRoomPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setValueDetail();
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) ReservationDetailsPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
    @FXML
    private void getAll() {
        try {
            ObservableList<ReservationDetailsTM> obList = FXCollections.observableArrayList();
            List<ReservationDetails> resList = ReservationDetailsModel.getAll();

            for (ReservationDetails reservationDetails : resList) {
                obList.add(new ReservationDetailsTM(
                        reservationDetails.getRes_ID(),
                        reservationDetails.getRoom_ID(),
                        reservationDetails.getRoom_price(),
                        reservationDetails.getGuest_ID(),
                        reservationDetails.getGuest_Name(),
                        reservationDetails.getCheck_In_Date(),
                        reservationDetails.getCheck_Out_Date()
                ));
            }
            tblReservationDetails.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    private void setValueDetail() {
        colReservationId.setCellValueFactory(new PropertyValueFactory<>("Res_ID"));
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("Room_ID"));
        colRoomPrice.setCellValueFactory(new PropertyValueFactory<>("Room_price"));
        colCheckinDate.setCellValueFactory(new PropertyValueFactory<>("Check_In_Date"));
        colCheckoutDate.setCellValueFactory(new PropertyValueFactory<>("Check_Out_Date"));
        colGuestId.setCellValueFactory(new PropertyValueFactory<>("Guest_ID"));
        colGuestName.setCellValueFactory(new PropertyValueFactory<>("Guest_Name"));
    }
    public void txtSearchOnAction(ActionEvent actionEvent) {
        if(!txtSearch.getText().isBlank()){
            getDetails();
        }else {new Alert(Alert.AlertType.ERROR, "Fill Reservation ID!").show();}
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        if(!txtSearch.getText().isBlank()){
            getDetails();
        }else {new Alert(Alert.AlertType.ERROR, "Fill Reservation ID!").show();}
    }

    public void getDetails() {
        tblReservationDetails.getItems().stream()
                .filter(item -> item.getRes_ID().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblReservationDetails.getSelectionModel().select(item);
                    tblReservationDetails.scrollTo(item);
                    txtSearch.clear();
                });
    }
}
