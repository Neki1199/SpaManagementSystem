module com.spams.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.sms to javafx.fxml;
    opens com.sms.Controllers to javafx.fxml;
    opens com.sms.BackEnd;
    exports com.sms;
    exports com.sms.Controllers;
}