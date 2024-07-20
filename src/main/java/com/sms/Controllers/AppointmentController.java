package com.sms.Controllers;

import com.sms.Controllers.Calendar.AddAppointmentView;
import com.sms.Controllers.Calendar.DayView;
import com.sms.Controllers.Calendar.SearchEditAppointmentView;
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
    public ChoiceBox<String> hourBox;
    public ChoiceBox<String> minuteBox;
    public ChoiceBox<String> choiceBoxService;
    public ChoiceBox<String> choiceBoxEmployee;
    public ChoiceBox<String> serviceTypeBox;
    public DatePicker datePickerAddAppointment;
    public Button addClientBtn;
    public GridPane dialogAdd;
    public Button addAppointmentBtn;
    public Button cancelBtn;
    public Label errorLabel;

    public final ObservableList<TimeSlot> timeSlots = FXCollections.observableArrayList();
    public final DayView dayView = new DayView(this);
    public final AddAppointmentView appointmentView = new AddAppointmentView(this);
    final SearchEditAppointmentView searchEditAppointmentView = new SearchEditAppointmentView(this);
    public TextField searchClientField;
    public Button searchClientBtn;
    public ListView<String> clientListView;
    public TextField searchAppointmentField;
    public Button searchAppointmentBtn;
    public GridPane addClientGrid;
    public Label errorLabel1;
    public TextField nameField;
    public TextArea notesField;
    public TextField emailField;
    public TextField phoneField;
    public Button addClientBtn1;
    public Button cancelBtn1;
    public ListView<String> listViewClientSearch;
    public HBox boxUpdateDelete;
    public Button updateAppointment;
    public Button deleteAppointment;
    public Label errorLabel2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dayView.initializeDayView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        appointmentView.initializeView();
        searchEditAppointmentView.initializeView();


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
        if (employeeId == 1 || employeeId == 6) {
            color = "#fcabab";
        } else if (employeeId == 2 || employeeId == 7) {
            color = "#bce8bc";
        } else if (employeeId == 3 || employeeId == 8) {
            color = "#7a7af1";
        } else if (employeeId == 4 || employeeId == 9) {
            color = "#facaa1";
        } else if (employeeId == 5 || employeeId == 10) {
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