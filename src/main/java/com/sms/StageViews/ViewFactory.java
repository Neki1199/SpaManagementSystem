package com.sms.StageViews;

import com.sms.Controllers.AdminController;
import com.sms.Controllers.StaffController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


// Methods for showing Views
public class ViewFactory {

    // flags to know which button on menu is selected, to change the Center in Admin and Staff.fxml
    private final StringProperty adminSelectedMenuItem;
    private final StringProperty staffSelectedMenuItem;

    private AnchorPane appointmentsDashboard;
    private AnchorPane clientsDashboard;
    private AnchorPane servicesDashboard;
    private AnchorPane inventoryDashboard;
    private AnchorPane staffDashboard;
    private AnchorPane invoicesDashboard;
    private AnchorPane reportsDashboard;

    public ViewFactory() {
        this.adminSelectedMenuItem = new SimpleStringProperty("");
        this.staffSelectedMenuItem = new SimpleStringProperty("");
    }

    // Method to get the AnchorPane to be attached with the staff and admin menu
    public AnchorPane getAppointmentsView() {
        if (appointmentsDashboard == null) {
            try {
                appointmentsDashboard = new FXMLLoader(getClass().getResource("/Fxml/Appointments.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return appointmentsDashboard;
    }

    public AnchorPane getClientsView() {
        if (clientsDashboard == null) {
            try {
                clientsDashboard = new FXMLLoader(getClass().getResource("/Fxml/Clients.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return clientsDashboard;
    }

    public AnchorPane getServicesView() {
        if (servicesDashboard == null) {
            try {
                servicesDashboard = new FXMLLoader(getClass().getResource("/Fxml/Services.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return servicesDashboard;
    }

    public AnchorPane getInventoryView() {
        if (inventoryDashboard == null) {
            try {
                inventoryDashboard = new FXMLLoader(getClass().getResource("/Fxml/Inventory.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return inventoryDashboard;
    }

    public AnchorPane getStaffView() {
        if (staffDashboard == null) {
            try {
                staffDashboard = new FXMLLoader(getClass().getResource("/Fxml/StaffButtonInMenu.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return staffDashboard;
    }

    public AnchorPane getInvoicesView() {
        if (invoicesDashboard == null) {
            try {
                invoicesDashboard = new FXMLLoader(getClass().getResource("/Fxml/Invoices.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return invoicesDashboard;
    }

    public AnchorPane getReportsView() {
        if (reportsDashboard == null) {
            try {
                reportsDashboard = new FXMLLoader(getClass().getResource("/Fxml/Reports.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return reportsDashboard;
    }



    // Method to show the Login View
    public void showLogin(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStageNonResizable(loader);
    }

    public void showStaffAppointments(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Staff.fxml"));
        StaffController controller = new StaffController();
        loader.setController(controller);
        createStage(loader);
    }

    public void showAdminAppointments(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin.fxml"));
        AdminController controller = new AdminController();
        loader.setController(controller);
        createStage(loader);
    }

    public void showAdminClients(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ClientsAdmin.fxml"));
        createStage(loader);
    }
    public void showStaffClients(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ClientsStaff.fxml"));
        createStage(loader);
    }

    public void showAdminServices(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ServicesAdmin.fxml"));
        createStage(loader);
    }
    public void showStaffServices(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ServicesStaff.fxml"));
        createStage(loader);
    }

    public void showInventory(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Inventory.fxml"));
        createStage(loader);
    }

    public void showStaffOnMenu(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StaffButtonInMenu.fxml"));
        createStage(loader);
    }

    public void showInvoices(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Invoices.fxml"));
        createStage(loader);
    }

    public void showReports(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Reports.fxml"));
        createStage(loader);
    }



    private static void createStage(FXMLLoader loader) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e) {throw new RuntimeException(e);}
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Spa Management System");
        stage.show();
    }

    private static void createStageNonResizable(FXMLLoader loader) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e) {throw new RuntimeException(e);}
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Spa Management System");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }

    public StringProperty adminSelectedMenuItemProperty() {
        return adminSelectedMenuItem;
    }

    public StringProperty staffSelectedMenuItemProperty() {
        return staffSelectedMenuItem;
    }
}
