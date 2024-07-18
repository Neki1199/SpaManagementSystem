package com.sms.Controllers;

import com.sms.Controllers.Calendar.AddAppointmentView;
import com.sms.Controllers.Calendar.DayView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import com.sms.Models.*;
import javafx.scene.layout.Pane;


// Controls calendar UI

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
    public ChoiceBox<String> serviceTypeBox;
    public DatePicker datePickerAddAppointment;
    public Button addClientBtn;
    public GridPane dialogAdd;
    public Button addAppoitnmentBtn;
    public Button cancelBtn;
    public Label errorLabel;

    public final ObservableList<TimeSlot> timeSlots = FXCollections.observableArrayList();
    public final DayView dayView = new DayView(this);
    final AddAppointmentView appointmentView = new AddAppointmentView(this);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dayView.initializeDayView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


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
        if (employeeId == 1) {
            color = "#fcabab";
        } else if (employeeId == 2) {
            color = "#bce8bc";
        } else if (employeeId == 3) {
            color = "#7a7af1";
        } else if (employeeId == 4) {
            color = "#facaa1";
        } else if (employeeId == 5) {
            color = "#a171a1";
        }
        return color;
    }

    // Represents a row in table
    public static class TimeSlot {
        public final String time;
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