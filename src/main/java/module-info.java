module com.spams.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.sms to javafx.fxml;
    opens com.sms.Controllers to javafx.fxml;
    opens com.sms.Models;
    exports com.sms;
    exports com.sms.Controllers;
}