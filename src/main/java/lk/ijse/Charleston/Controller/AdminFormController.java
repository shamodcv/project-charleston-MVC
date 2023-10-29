package lk.ijse.Charleston.Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Charleston.dto.Admin;
import lk.ijse.Charleston.model.AdminModel;

import java.io.IOException;
import java.sql.SQLException;

public class AdminFormController {

    public JFXTextField txtOldPassword;
    public JFXTextField txtNewUserName;
    public JFXPasswordField txtNewPassword;
    public AnchorPane AdminPane;

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));

        Stage stage = (Stage) AdminPane.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));

        Stage stage = (Stage) AdminPane.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String olduser = txtOldPassword.getText();
        String usename = txtNewUserName.getText();
        String pass = txtNewPassword.getText();
        if (!txtOldPassword.getText().isBlank() && !txtNewUserName.getText().isBlank() && !txtNewPassword.getText().isBlank()) {
            try {
                Admin admin = new Admin(usename, pass);
                boolean isUpdated = AdminModel.adminUpdate(admin, olduser);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Username & Password is updated!").show();
                    txtOldPassword.setText("");
                    txtNewUserName.setText("");
                    txtNewPassword.setText("");
                } else if (isUpdated == false) {
                    new Alert(Alert.AlertType.ERROR, "Invaild Password!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show();
        }
    }
}
