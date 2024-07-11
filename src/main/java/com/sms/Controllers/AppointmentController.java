package com.sms.Controllers;

import com.sms.Calendar.DayView;
import com.sms.DAO.AptDAOImplement;
import com.sms.DAO.DAO;
import com.sms.DAO.EmpDAOImplement;
import com.sms.DAO.EmployeeDAO;
import javafx.beans.property.SimpleObjectProperty;

import com.sms.BackEnd.Client;
import com.sms.BackEnd.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.sms.BackEnd.Employee;
import com.sms.BackEnd.Appointment;


// Will control calendar UI

public class AppointmentController extends Node implements Initializable {

    public HBox hbox;
    public TableView<TimeSlot> appointmentTable;
    public TableColumn<TimeSlot, String> timeColumn;
    public DatePicker datePicker;
    public ListView<Label> listView;
    public Button add;
    public Button edit;
    public Button delete;
    public List<Employee> employees;
    public DatePicker datePicker1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DayView dayView = new DayView(this);
            dayView.initializeDayView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize other views here
//        initializeWeekView();
//        initializeMonthView();
//        initializeListAllView();
    }

    // To get the label color of the Employee doing that service
    public static String getColorForEmployee(int employeeId) {
        String color = "";
        if (employeeId == 0) {
            color = "#FF6666";
        } else if (employeeId == 1) {
            color = "#3F8A3F";
        } else if (employeeId == 2) {
            color = "#4545F1";
        } else if (employeeId == 3) {
            color = "#FCB66D";
        } else if (employeeId == 4) {
            color = "#793979";
        }
        return color;
    }

    // Represents a row in table
    public static class TimeSlot {
        private final String time;
        public final Map<String, Label> appointmentDetails;

        public TimeSlot(String time) {
            this.time = time;
            this.appointmentDetails = new HashMap<>();
        }

        public Label getAppointmentDetails(String employeeName) {
            return appointmentDetails.get(employeeName);
        }
        public void setAppointmentDetails(String employeeName, Label label) {
            appointmentDetails.put(employeeName, label);
        }
        public String getTime() {
            return time;
        }


    }}