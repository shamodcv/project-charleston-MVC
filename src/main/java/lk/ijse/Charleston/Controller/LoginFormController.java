package lk.ijse.Charleston.Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Charleston.dto.Admin;
import lk.ijse.Charleston.model.AdminModel;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane LoginPane;
    public JFXPasswordField txtPassword;
    public JFXTextField txtUserName;

    public void btnLoginOnAction(ActionEvent actionEvent) {
        if(!txtUserName.getText().isBlank() && !txtPassword.getText().isBlank()) {
            try {
                String name = txtUserName.getText();
                String pass = txtPassword.getText();
                Admin admin  = new Admin(name,pass);
                boolean isSaved = AdminModel.check(admin);
                if (isSaved){
                    Stage stage = (Stage) LoginPane.getScene().getWindow();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"))));
                    stage.setTitle("Dashboard");
                    stage.centerOnScreen();
                    stage.show();
                }
                //else if (isSaved == false){new Alert(Alert.AlertType.ERROR, " Username or Password Invaild!").show();}
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                // new Alert(Alert.AlertType.ERROR, "Username or Password Invaild!").show();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }

    }

    public void userNameOnAction(ActionEvent actionEvent) {
        if (!txtUserName.getText().isBlank()){
            txtPassword.requestFocus();

        }
    }

    public void passwordOnAction(ActionEvent actionEvent) {
        if (!txtPassword.getText().isBlank()){
            txtUserName.requestFocus();
        }
    }
}
