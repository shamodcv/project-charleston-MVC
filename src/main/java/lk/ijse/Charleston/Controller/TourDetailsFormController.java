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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Charleston.db.DBConnection;
import lk.ijse.Charleston.dto.*;
import lk.ijse.Charleston.dto.tm.FoodTM;
import lk.ijse.Charleston.dto.tm.TourDetailsTM;
import lk.ijse.Charleston.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TourDetailsFormController {

    @FXML
    private JFXComboBox <String> cmbType;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtNo;
    @FXML
    private TextField txtCountry;
    @FXML
    private TextField txtEmail;
    @FXML
    private AnchorPane TourDetailsPane;
    @FXML
    private JFXComboBox <String> cmbTourId;
    @FXML
    private Label lblGuestName;
    @FXML
    private Label lblTourName;
    @FXML
    private Label lblPrice;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView <TourDetailsTM>tblTourDetails;
    @FXML
    private TableColumn colGuestId;
    @FXML
    private TableColumn colGuestName;
    @FXML
    private TableColumn colTourId;
    @FXML
    private TableColumn colTourName;
    @FXML
    private TableColumn colPrice;
    @FXML
    private TableColumn colBooking;
    @FXML
    private TextField txtBookId;

    public void initialize() {
        ObservableList<String> titleList = FXCollections.observableArrayList("ID card","Passport");
        cmbType.setItems(titleList);
        loadTourIds();
        getAll();
        setValueFactory();
        setSelectToTxt();
        generateNextResId();
    }

    @FXML
    private void getAll() {
        try {
            ObservableList<TourDetailsTM> obList = FXCollections.observableArrayList();
            List<TourDetails> tourList = TourDetailModel.getAll();

            for (TourDetails tourDetails : tourList) {
                obList.add(new TourDetailsTM(
                        tourDetails.getBookID(),
                        tourDetails.getGuestID(),
                        tourDetails.getGuestName(),
                        tourDetails.getTourID(),
                        tourDetails.getTourName(),
                        tourDetails.getPrice()
                ));
            }
            tblTourDetails.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    private void setSelectToTxt() {
        tblTourDetails.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtBookId.setText(newSelection.getBookID());
                txtId.setText(newSelection.getGuestID());
                lblGuestName.setText(newSelection.getGuestName());
                lblTourName.setText(newSelection.getTourName());
                lblPrice.setText(String.valueOf(newSelection.getPrice()));
                cmbTourId.setValue(String.valueOf(newSelection.getTourID()));
            }
        });
    }
    @FXML
    private void setValueFactory() {
    colGuestId.setCellValueFactory(new PropertyValueFactory<>("guestID"));
    colGuestName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
    colTourId.setCellValueFactory(new PropertyValueFactory<>("tourID"));
    colTourName.setCellValueFactory(new PropertyValueFactory<>("tourName"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colBooking.setCellValueFactory(new PropertyValueFactory<>("bookID"));
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) TourDetailsPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        String Bookid = txtBookId.getText();
        String guestId = txtId.getText();
        String guestName = lblGuestName.getText();
        String tourId = cmbTourId.getValue();
        String tourName = lblTourName.getText();
        Double price = Double.valueOf(lblPrice.getText());

        String type = cmbType.getValue();
        String contact = txtNo.getText();
        String country = txtCountry.getText();
        String email = txtEmail.getText();

        TourDetails tourDetails = new TourDetails(Bookid, guestId, guestName, tourId, tourName, price);
        try {
            Guest searchGuest = GuestModel.searchById(guestId);
            if(searchGuest == null) {
                boolean guestSave = GuestModel.save(new Guest(type, guestId, guestName, contact, country, email));
                if(guestSave){
                    boolean isSaved = TourDetailModel.save(tourDetails);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "TourDetail saved!").show();
                        getAll();
                        setValueFactory();
                        generateNextResId();
                    }
                }else {
                    new Alert(Alert.AlertType.CONFIRMATION, "Guest Not saved!").show();
                }
            }else{
                boolean isSaved = TourDetailModel.save(tourDetails);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Guest Alredy Registerd! TourDetail saved!").show();
                    getAll();
                    setValueFactory();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "TourDetail Not saved ,Please Check Details & Try Again!").show();
            txtClear();
        }
    }

    private void txtClear() {
        txtBookId.setText("");
        txtId.setText("");
        cmbTourId.setValue(null);
        lblTourName.setText("");
        lblPrice.setText("");
        cmbType.setValue(null);
        txtNo.setText("");
        txtCountry.setText("");
        txtEmail.setText("");
        txtName.setText("");
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtClear();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String Bookid = txtBookId.getText();
        String guestId = txtId.getText();
        String guestName = lblGuestName.getText();
        String tourId = cmbTourId.getValue();
        String tourName = lblTourName.getText();
        Double price = Double.valueOf(lblPrice.getText());

        TourDetails tourDetails = new TourDetails(Bookid, guestId, guestName, tourId, tourName, price);
        try {
            boolean isUpdated = TourDetailModel.update(tourDetails);
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "TourDetail updated!").show();
                getAll();
                setValueFactory();
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Booking Id Not Exist!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String Bookid = txtBookId.getText();
        try {
            boolean isDeleted = TourDetailModel.delete(Bookid);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "TourDetail Deleted!").show();
                txtClear();
                getAll();
                setValueFactory();
            } else {
                new Alert(Alert.AlertType.ERROR, "Booking Id Not Exist!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
            txtClear();
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        tblTourDetails.getItems().stream()
                .filter(item -> item.getBookID().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblTourDetails.getSelectionModel().select(item);
                    tblTourDetails.scrollTo(item);
                    txtSearch.clear();
                });
    }

    private void loadTourIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> ids = TourModel.loadIds();

            for (String id : ids) {
                obList.add(id);
            }
            cmbTourId.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    public void GuestOnAction(ActionEvent actionEvent) {
        String code = txtId.getText();
        try {
            Guest guest = GuestModel.searchById(code);
            fillGuestFields(guest);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    public void tourOnAction(ActionEvent actionEvent) {
        String code = cmbTourId.getValue();
        try {
            Tour tour = TourModel.searchById(code);
            fillTourFields(tour);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void fillTourFields(Tour tour) {
        lblTourName.setText(tour.getTourName());
        lblPrice.setText(String.valueOf(tour.getTourPrice()));
    }

    private void fillGuestFields(Guest guest) {
        txtName.setText(guest.getName());
        cmbType.setValue(guest.getIdType());
        txtNo.setText(guest.getContactNo());
        txtCountry.setText(guest.getCountry());
        txtEmail.setText(guest.getEmail());
    }
    private void generateNextResId() {
        try {
            String id = TourDetailModel.getNextResId();
            txtBookId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    public void btnPrintOnAction(ActionEvent actionEvent) {
        if(!txtBookId.getText().isEmpty() && txtBookId.getText()!=null){

            try {
                JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/jasper/Tour.jrxml");
                JRDesignQuery query = new JRDesignQuery();
                query.setText("SELECT tour_details.Book_ID, tour_details.Guest_ID, tour_details.Guest_Name, tour_details.Tour_ID, tour_details.Tour_Name,tour_details.Price FROM tour_details INNER JOIN guest ON tour_details.Guest_ID=guest.Guest_ID INNER JOIN tour ON tour_details.tour_ID=tour.tour_ID WHERE Book_ID ='"+txtBookId.getText()+"';");
                jasperDesign.setQuery(query);

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint,false);
            } catch (JRException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
