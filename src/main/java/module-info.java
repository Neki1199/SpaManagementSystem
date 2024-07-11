module com.spams.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;


    opens com.sms to javafx.fxml;
    opens com.sms.Controllers to javafx.fxml;
    opens com.sms.BackEnd;
    exports com.sms;
    exports com.sms.Controllers;
    exports com.sms.DataModels;
    exports com.sms.StageViews;
}