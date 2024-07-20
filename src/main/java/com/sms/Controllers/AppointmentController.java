package com.sms.Controllers;

import com.sms.Controllers.Calendar.AddAppointmentView;
import com.sms.Controllers.Calendar.DayView;
import com.sms.Controllers.Calendar.SearchEditAppointmentView;
import com.sms.Models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AppointmentController extends Node implements Initializable {
    public HBox hbox;
    public TableView<TimeSlot> appointmentTable;
    public TableColumn<TimeSlot, String> timeColumn;
    public DatePicker datePicker, datePicker1, datePickerAddAppointment;
    public ListView<Label> listView;
    public Button add, edit, delete, addClientBtn, addAppointmentBtn, cancelBtn;
    public Button searchClientBtn, searchAppointmentBtn, addClientBtn1, cancelBtn1;
    public Button updateAppointment, deleteAppointment;
    public ChoiceBox<String> hourBox, minuteBox, choiceBoxService, choiceBoxEmployee, serviceTypeBox;
    public Label errorLabel, errorLabel1, errorLabel2;
    public TextField searchClientField, searchAppointmentField, nameField, emailField, phoneField;
    public TextArea notesField;
    public ListView<String> clientListView, listViewClientSearch;
    public GridPane dialogAdd, addClientGrid;
    public HBox boxUpdateDelete;
    public List<Employee> employees;

    public final ObservableList<TimeSlot> timeSlots = FXCollections.observableArrayList();
    public final DayView dayView = new DayView(this);
    public final AddAppointmentView appointmentView = new AddAppointmentView(this);
    public final SearchEditAppointmentView searchEditAppointmentView = new SearchEditAppointmentView(this);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dayView.initializeDayView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentView.initializeView();
        searchEditAppointmentView.initializeView();
    }

    // To get the label color of the Employee doing that service
    public static String getColorForEmployee(int employeeId) {
        return switch (employeeId) {
            case 1, 6 -> "#fcabab";
            case 2, 7 -> "#bce8bc";
            case 3, 8 -> "#7a7af1";
            case 4, 9 -> "#facaa1";
            case 5, 10 -> "#a171a1";
            default -> "";
        };
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
