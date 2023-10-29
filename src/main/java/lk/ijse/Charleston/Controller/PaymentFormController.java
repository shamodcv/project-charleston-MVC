package lk.ijse.Charleston.Controller;

import com.jfoenix.controls.JFXButton;
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
import lk.ijse.Charleston.dto.Food;
import lk.ijse.Charleston.dto.Payment;
import lk.ijse.Charleston.dto.ReservationDetails;
import lk.ijse.Charleston.dto.tm.FoodTM;
import lk.ijse.Charleston.dto.tm.PaymentTM;
import lk.ijse.Charleston.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class PaymentFormController {
   @FXML
   private AnchorPane PaymentPane;
   @FXML
   private TextField txtSearch;
   @FXML
   private Label lblGuestId;
   @FXML
   private Label lblGuestName;
   @FXML
   private Label lblRoomId;
   @FXML
   private Label lblCheckinDate;
   @FXML
   private Label lblCheckoutDate;
   @FXML
   private Label lblTotalPrice;
   @FXML
   private TextField txtPaymentId;
   @FXML
   private TextField txtReservationId;
   @FXML
   private Label lblOrderAm;

    public TableView <PaymentTM> tblPayment;
    @FXML
    private TableColumn colPaymentId;
    @FXML
    private TableColumn colGuestId;
    @FXML
    private TableColumn colGuestName;
    @FXML
    private TableColumn colReservation;
    @FXML
    private TableColumn colRoomId;
    @FXML
    private TableColumn colCheckinDate;
    @FXML
    private TableColumn colCheckoutDate;
    @FXML
    private TableColumn colOrderAm;
    @FXML
    private TableColumn colTotalPrice;

    public void initialize() {
        getAll();
        setValueFactory();
        setSelectToTxt();
        generateNextId();
    }
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) PaymentPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
    @FXML
    private void setValueFactory() {
          colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
          colGuestId.setCellValueFactory(new PropertyValueFactory<>("guestId"));
          colGuestName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
          colReservation.setCellValueFactory(new PropertyValueFactory<>("resId"));
          colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
          colCheckinDate.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
          colCheckoutDate.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
          colOrderAm.setCellValueFactory(new PropertyValueFactory<>("orderAm"));
          colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));

    }
    @FXML
    private void getAll() {
        try {
            ObservableList<PaymentTM> obList = FXCollections.observableArrayList();
            List<Payment> paymentList = PaymentModel.getAll();

            for (Payment payment : paymentList) {
                obList.add(new PaymentTM(
                        payment.getPaymentId(),
                        payment.getGuestId(),
                        payment.getGuestName(),
                        payment.getResId(),
                        payment.getRoomId(),
                        payment.getCheckIn(),
                        payment.getCheckOut(),
                        payment.getOrderAm(),
                        payment.getTotal()
                ));
            }
            tblPayment.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    public void txtSearchOnAction(ActionEvent actionEvent) {
        search();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        search();
    }
    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        String paymentId = txtPaymentId.getText();
        String guestId = lblGuestId.getText();
        String guestName = lblGuestName.getText();
        String reservationId = txtReservationId.getText();
        String roomId = lblRoomId.getText();
        String checkinDate = lblCheckinDate.getText();
        String checkoutDate = lblCheckoutDate.getText();
        Double ordersAm = Double.valueOf(lblOrderAm.getText());
        Double totalPrice = Double.valueOf(lblTotalPrice.getText());
        String release = "Available";

        Payment payment = new Payment(paymentId,guestId,guestName,reservationId,roomId,checkinDate,checkoutDate,ordersAm,totalPrice);
        try {
            boolean isSaved = PaymentModel.save(payment);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment saved!").show();
                RoomModel.releaseRoom(roomId,release);
                getAll();
                setValueFactory();
                generateNextId();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Payment Not saved ,Please Check Details & Try Again!").show();
            clearTxt();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearTxt();
    }
    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String paymentId = txtPaymentId.getText();
        String guestId = lblGuestId.getText();
        String guestName = lblGuestName.getText();
        String reservationId = txtReservationId.getText();
        String roomId = lblRoomId.getText();
        String checkinDate = lblCheckinDate.getText();
        String checkoutDate = lblCheckoutDate.getText();
        Double ordersAm = Double.valueOf(lblOrderAm.getText());
        Double totalPrice = Double.valueOf(lblTotalPrice.getText());

        Payment payment = new Payment(paymentId,guestId,guestName,reservationId,roomId,checkinDate,checkoutDate,ordersAm,totalPrice);
        try {
            boolean isUpdated = PaymentModel.update(payment);
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Payment updated!").show();
                getAll();
                setValueFactory();
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Payment Id Not Exist!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
        }
    }
    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String paymentId = txtPaymentId.getText();
        try {
            boolean isDeleted = PaymentModel.delete(paymentId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Deleted!").show();
                clearTxt();
                getAll();
                setValueFactory();
            } else {
                new Alert(Alert.AlertType.ERROR, "Payment Id Not Exist!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
            clearTxt();
        }
    }
    @FXML
    public void resOnAction(){
        String code = txtReservationId.getText();
        try {
            ReservationDetails res = ReservationDetailsModel.searchById(code);
           Double orderAm = getOrderAmmount(code);

           LocalDate checkIn = LocalDate.parse(res.getCheck_In_Date());
           LocalDate checkOut = LocalDate.parse(res.getCheck_Out_Date());
           Double price = Double.valueOf(res.getRoom_price());
           Double roomPrice = calculateRoomPrice(checkIn,checkOut,price);

           Double finalAmmount = orderAm+roomPrice;
            fillResFields(res,orderAm,finalAmmount);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    @FXML
    private void fillResFields(ReservationDetails res,Double orderAm,Double totalAm) {
        lblGuestId.setText(res.getGuest_ID());
        lblRoomId.setText(String.valueOf(res.getRoom_ID()));
        lblGuestName.setText(String.valueOf(res.getGuest_Name()));
        lblCheckinDate.setText(res.getCheck_In_Date());
        lblCheckoutDate.setText(res.getCheck_Out_Date());
        lblOrderAm.setText(String.valueOf(orderAm));
        lblTotalPrice.setText(String.valueOf(totalAm));
    }

    public void resIdOnAction(ActionEvent actionEvent) {
        resOnAction();
    }
    public Double getOrderAmmount(String resId) {
        Double orderAmmount = 0.0;
        try {
            orderAmmount = PlaceOrderModel.getOrderAm(resId);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
            return orderAmmount;
    }
    public static double calculateRoomPrice(LocalDate checkin, LocalDate checkout, Double price) {
        long days = ChronoUnit.DAYS.between(checkin, checkout);
        return price * days;
    }
    @FXML
    private void clearTxt(){
        txtPaymentId.setText("");
        lblGuestId.setText("");
        lblGuestName.setText("");
        txtReservationId.setText("");
        lblRoomId.setText("");
        lblCheckinDate.setText("");
        lblCheckoutDate.setText("");
        lblOrderAm.setText("");
        lblTotalPrice.setText("");

    }
    @FXML
    private void search(){
        tblPayment.getItems().stream()
                .filter(item -> item.getPaymentId().equals(txtSearch.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tblPayment.getSelectionModel().select(item);
                    tblPayment.scrollTo(item);
                    txtSearch.clear();
                });

    }
    @FXML
    private void setSelectToTxt() {
        tblPayment.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtPaymentId.setText(newSelection.getPaymentId());
                lblGuestId.setText(newSelection.getGuestId());
                lblGuestName.setText(newSelection.getGuestName());
                txtReservationId.setText(newSelection.getResId());
                lblRoomId.setText(newSelection.getRoomId());
                lblCheckinDate.setText(newSelection.getCheckIn());
                lblCheckoutDate.setText(newSelection.getCheckOut());
                lblOrderAm.setText(String.valueOf(newSelection.getOrderAm()));
                lblTotalPrice.setText(String.valueOf(newSelection.getTotal()));
            }
        });
    }
    @FXML
    public void btnPrintOnAction(ActionEvent actionEvent) {
        if(!txtPaymentId.getText().isEmpty() && txtPaymentId.getText()!=null){

            try {
                JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/jasper/Report.jrxml");
                JRDesignQuery query = new JRDesignQuery();
                query.setText("select payment. PayID, payment.Guest_ID, payment.Guest_Name, payment.Res_ID, payment.Room_ID, payment.Check_in_Date, payment.Check_out_Date, payment.Orders_Ammount, payment.Total_Price FROM payment INNER JOIN guest ON payment.Guest_ID=guest.Guest_ID INNER JOIN  Resavations ON payment.Res_ID=Resavations.Res_ID INNER JOIN room ON payment.Room_ID=room.Room_ID WHERE PayID='"+txtPaymentId.getText()+"';");
                jasperDesign.setQuery(query);

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint,false);
            } catch (JRException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void generateNextId() {
        try {
            String id = PaymentModel.getNextId();
            txtPaymentId.setText(id);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
}
