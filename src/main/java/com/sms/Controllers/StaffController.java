package com.sms.Controllers;

import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    public BorderPane staff_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // listener of SelectedMenuItem to know which menu button is selected, to change the center of admin and staff.fxml
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case "Appointments" -> staff_parent.setCenter(Model.getInstance().getViewFactory().getAppointmentsView());
                case "Clients" -> staff_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
                case "Services" -> staff_parent.setCenter(Model.getInstance().getViewFactory().getServicesView());
                default -> staff_parent.setCenter(Model.getInstance().getViewFactory().getAppointmentsView());
            }
        });
    }
}