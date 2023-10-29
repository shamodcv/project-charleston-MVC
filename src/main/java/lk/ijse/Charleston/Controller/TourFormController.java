package lk.ijse.Charleston.Controller;

import com.jfoenix.controls.JFXButton;
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
import lk.ijse.Charleston.dto.Room;
import lk.ijse.Charleston.dto.Tour;
import lk.ijse.Charleston.dto.tm.TourTM;
import lk.ijse.Charleston.model.RoomModel;
import lk.ijse.Charleston.model.TourModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TourFormController {


    @FXML
    private AnchorPane TourPane;

    @FXML
    private TextField txtTourId;

    @FXML
    private TextField txtTourName;

    @FXML
    private TextField txtTourDescription;

    @FXML
    private TextField txtTourPrice;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TextField txtSearch;


    public TableView<TourTM> tblTour;

    @FXML
    private TableColumn colTourID;

    @FXML
    private TableColumn colTourName;

    @FXML
    private TableColumn colTourDescription;

    @FXML
    private TableColumn colPrice;

    public void initialize() {

        setCellValueFactory();
        setSelectStaffToTxt();
        getAll();
    }
    @FXML
    private void setCellValueFactory() {
        colTourID.setCellValueFactory(new PropertyValueFactory<>("tourId"));
        colTourName.setCellValueFactory(new PropertyValueFactory<>("tourName"));
        colTourDescription.setCellValueFactory(new PropertyValueFactory<>("tourDescription"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("tourPrice"));
    }
    @FXML
    private void getAll() {
        try {
            ObservableList<TourTM> obList = FXCollections.observableArrayList();
            List<Tour> tourList = TourModel.getAll();

            for (Tour tour : tourList) {
                obList.add(new TourTM(
                        tour.getTourId(),
                        tour.getTourName(),
                        tour.getTourDescription(),
                        tour.getTourPrice()
                ));
            }
            tblTour.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    private void setSelectStaffToTxt() {
        tblTour.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtTourId.setText(newSelection.getTourId());
                txtTourName.setText(newSelection.getTourName());
                txtTourDescription.setText(newSelection.getTourDescription());
                txtTourPrice.setText(String.valueOf(newSelection.getTourPrice()));

            }
        });
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) TourPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {

        if(!txtTourId.getText().isBlank() && !txtTourName.getText().isBlank() && !txtTourDescription.getText().isBlank() && !txtTourPrice.getText().isBlank()) {
            String tourId = txtTourId.getText();
            String tourName = txtTourName.getText();
            String tourDescription= txtTourDescription.getText();
            double tourPrice = Double.parseDouble(txtTourPrice.getText());

            Tour tour = new Tour(tourId,tourName,tourDescription,tourPrice);
            try {
                boolean isSaved = TourModel.save(tour);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Tour saved!").show();
                    txtClear();
                    getAll();
                    setCellValueFactory();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Tour Not saved ,Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String tourId = txtTourId.getText();
        String tourName = txtTourName.getText();
        String tourDescription = txtTourDescription.getText();
        double tourPrice = Double.parseDouble(txtTourPrice.getText());

        Tour tour = new Tour(tourId,tourName,tourDescription,tourPrice);
        try {
            boolean isUpdated = TourModel.update(tour);
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Tour updated!").show();
                txtClear();
                setCellValueFactory();
                getAll();
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Tour Id Not Exist!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Tour Id Not Exist!").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String tourId = txtTourId.getText();
        if(!txtTourId.getText().isBlank()) {
            try {
                boolean isDeleted = TourModel.delete(tourId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Tour Deleted!").show();
                    txtClear();
                    getAll();
                    setCellValueFactory();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Tour Id Not Exist!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                 txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Tour Id!").show(); }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        btnSearchOnAction(actionEvent);

    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        tblTour.getItems().stream()
                .filter(item -> item.getTourId().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblTour.getSelectionModel().select(item);
                    tblTour.scrollTo(item);
                    txtSearch.clear();
                });
        tblTour.getItems().stream()
                .filter(item -> item.getTourName().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblTour.getSelectionModel().select(item);
                    tblTour.scrollTo(item);
                    txtSearch.clear();
                });
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtClear();
    }

    private void txtClear() {
        txtTourId.clear();
        txtTourName.clear();
        txtTourDescription.clear();
        txtTourPrice.clear();
    }
}
