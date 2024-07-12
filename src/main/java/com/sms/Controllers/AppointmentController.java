package com.sms.Controllers;

import com.sms.Calendar.AddAppointmentView;
import com.sms.Calendar.DayView;
import com.sms.DAO.*;

import com.sms.BackEnd.Client;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import com.sms.BackEnd.*;
import javafx.scene.layout.Pane;


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
    public ChoiceBox<String> choiceBoxClient;
    public ChoiceBox<String> hourBox;
    public ChoiceBox<String> minuteBox;
    public ChoiceBox<String> choiceBoxService;
    public ChoiceBox<String> choiceBoxEmployee;
    public DatePicker datePickerAddAppointment;
    public Button addClientBtn;
    public Pane dialogAdd;
    public Button addAppoitnmentBtn;
    public Button cancelBtn;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DayView dayView = new DayView(this);
            dayView.initializeDayView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AddAppointmentView appointmentView = new AddAppointmentView(this);
        appointmentView.initializeView();


//        try {
//            WeekView weekView = new WeekView(this);
//            weekView.initializeWeekView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            MonthView monthView = new MonthView(this);
//            monthView.initializeMonthView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            ListAllView listAllView = new ListAllView(this);
//            listAllView.initializeListAllView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

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
    }
}