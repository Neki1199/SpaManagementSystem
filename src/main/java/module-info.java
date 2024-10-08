module com.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires org.controlsfx.controls;


    opens com.sms to javafx.fxml;
    opens com.sms.Controllers to javafx.fxml;
    exports com.sms;
    exports com.sms.Controllers;
    exports com.sms.DAO;
    exports com.sms.Models;
    exports com.sms.Controllers.Calendar;
}