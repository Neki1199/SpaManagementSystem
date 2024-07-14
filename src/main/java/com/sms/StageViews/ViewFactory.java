package com.sms.StageViews;

import com.sms.Controllers.AdminController;
import com.sms.Controllers.StaffController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


// Methods for showing Views
public class ViewFactory {

    private AnchorPane menuStaffContent;
    private AnchorPane menuAdminContent;

    public ViewFactory() {}

    // Method to get the AnchorPane from the menu on StaffMenu.Fxml
    public AnchorPane getMenuStaffContent() {
        if (menuStaffContent == null) {
            try {
                menuStaffContent = new FXMLLoader(getClass().getResource("/Fxml/StaffMenu.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return menuStaffContent;
    }

    public AnchorPane getMenuAdminContent() {
        if (menuAdminContent == null) {
            try {
                menuAdminContent = new FXMLLoader(getClass().getResource("/Fxml/AdminMenu.fxml")).load();
            } catch (Exception e) {throw new RuntimeException(e);}
        }
        return menuAdminContent;
    }

    // Method to show the Login View
    public void showLogin(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStageNonResizable(loader);
    }

    public void showStaff(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Staff.fxml"));
        StaffController controller = new StaffController();
        loader.setController(controller);
        createStage(loader);
    }

    public void showAdmin(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin.fxml"));
        AdminController controller = new AdminController();
        loader.setController(controller);
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
}
