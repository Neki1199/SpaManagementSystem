package com.sms.Controllers;

import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField userInput;
    public PasswordField passwordInput;
    public Button loginButton;
    public Label loginErrorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Creates an instance of ViewFactory to show view of Staff (Staff Menu)
        // TODO Implement login features, so that it enters to staff or admin
        //loginButton.setOnAction(event -> Model.getInstance().getViewFactory().showStaff());
        loginButton.setOnAction(event -> Model.getInstance().getViewFactory().showAdmin());
    }
}
