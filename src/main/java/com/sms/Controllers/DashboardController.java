package com.sms.Controllers;

import com.sms.Controllers.Calendar.AddAppointmentView;
import com.sms.DAO.AppointmentDAO;
import com.sms.DAO.AptDAOImplement;
import com.sms.Models.Appointment;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label todayDate;
    public Label appointmentsNoLbl;
    public Label topProductLbl;
    public Label topServiceLbl;
    public ListView<String> inventoryListNotifications;
    private final AppointmentDAO appointmentDAO = new AptDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayDate.setText(String.valueOf(LocalDate.now()));
        try {
            getAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentsNoLbl.textProperty().bind(NotifyAppointmentsChanges.appointmentCountProperty().asString());
    }

    public void getAppointments() throws SQLException {
        String todayDate = String.valueOf(LocalDate.now());
        List<Appointment> appointments = appointmentDAO.getFromDate(todayDate);

        if(!appointments.isEmpty()){
            NotifyAppointmentsChanges.setAppointmentCount(appointments.size());
            appointmentsNoLbl.setText(String.valueOf(appointments.size()));
        }else{
            appointmentsNoLbl.setText("No appointments for today");
            NotifyAppointmentsChanges.setAppointmentCount(0);
        }
    }

//    private void getTopProduct(){
//
//    }
//
//    private void getTopService(){
//
//    }
}
