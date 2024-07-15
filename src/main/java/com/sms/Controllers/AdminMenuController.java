package com.sms.Controllers;

import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class AdminMenuController implements Initializable {
    public Button appointmentsBtn;
    public Button clientsBtn;
    public Button inventoryBtn;
    public Button staffBtn;
    public Button invoicesBtn;
    public Button reportsBtn;
    public Button configBtn;
    public Button logoutBtn;
    public Button servicesBtn;

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
        inventoryBtn.setOnAction(event -> onInventory());
        staffBtn.setOnAction(event -> onStaff());
        invoicesBtn.setOnAction(event -> onInvoices());
        reportsBtn.setOnAction(event -> onReports());
    }

    private void onReports() {
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Reports");
    }

    private void onInvoices() {
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Invoices");
    }

    private void onStaff() {
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Staff");
    }

    private void onInventory() {
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Inventory");
    }

    private void onServices() {
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Services");
    }

    private void onAppointment(){
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Appointments");
    }

    private void onClients(){
        Model.getInstance().getViewFactory().adminSelectedMenuItemProperty().set("Clients");
    }

}

