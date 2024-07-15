package com.sms.Controllers;

import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable{

    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // listener of SelectedMenuItem to know which menu button is selected, to change the center of admin and staff.fxml
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case "Appointments" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAppointmentsView());
                case "Clients" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
                case "Services" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getServicesView());
                case "Inventory" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getInventoryView());
                case "Staff" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getStaffView());
                case "Invoices" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getInvoicesView());
                case "Reports" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getReportsView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAppointmentsView());
            }
        });
    }
}
