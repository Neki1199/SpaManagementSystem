package com.sms.Controllers;

import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffMenuController implements Initializable {
    public Button appointmentsBtn;
    public Button clientsBtn;
    public Button servicesBtn;
    public Button configBtn;
    public Button logoutBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();

        logoutBtn.setOnAction(event -> {
            Stage stage = (Stage) appointmentsBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showLogin();
        });

    }

    private void addListeners(){
        appointmentsBtn.setOnAction(event -> onAppointment());
        clientsBtn.setOnAction(event -> onClients());
        servicesBtn.setOnAction(event -> onServices());
    }

    private void onAppointment(){
        Model.getInstance().getViewFactory().staffSelectedMenuItemProperty().set("Appointments");
    }

    private void onClients(){
        Model.getInstance().getViewFactory().staffSelectedMenuItemProperty().set("Clients");
    }

    private void onServices(){
        Model.getInstance().getViewFactory().staffSelectedMenuItemProperty().set("Services");
    }
}
