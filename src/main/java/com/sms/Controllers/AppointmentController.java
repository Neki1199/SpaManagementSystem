package com.sms.Controllers;

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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.sms.BackEnd.Employee;
import com.sms.BackEnd.Appointment;
import javafx.scene.layout.VBox;

import static com.sms.BackEnd.Employee.getEmployeeFromID;
import static com.sms.BackEnd.Employee.getEmployeesFromDatabase;

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

    private ObservableList<TimeSlot> timeSlots = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeDayView();
        // Initialize other views here
        initializeWeekView();
        initializeMonthView();
        initializeListAllView();
    }

    // For Day Tab
    private void initializeDayView() {
        // Create all columns and add the hours
        initializeColumnsAndData();
        // set initial date in DatPicker
        datePicker.setValue(LocalDate.now());
        // load appointments for initial date
        onDateSelected();
        // add listener to DatePicker
        datePicker.setOnAction(event -> onDateSelected());
    }

    // For Week Tab
    private void initializeWeekView() {
//        // Add appointments into ListView
//        listView.setItems(FXCollections.observableArrayList());
//        listView.getItems().clear();
//        List<Appointment> appointments = Appointment.getAppointmentForDate("ENTER DATA PICKER");
//        datePicker.setValue(LocalDate.now());
//
//        // load appointments for initial date
//        onDateSelected();
//        // add listener to DatePicker
//        datePicker.setOnAction(event -> onDateSelected());
//        for (Appointment appointment : appointments) {
//            // Add appointment details to ListView
//            Label labelList = new Label();
//            labelList.setText(appointment.toString());
//            isPaidColor(appointment, labelList);
//            listView.getItems().add(labelList);
//        }
        // CREAR LISTENING A  DATEPICKER1

        //        List<Employee> employees = getEmployeesFromDatabase();
//        // To create the labels at the bottom of the calendar, with colors
//        int colorIndex = 0;
//        for (Employee employee : employees) {
//            Label label = new Label(employee.getName());
//            label.setStyle("-fx-background-color: " + getColor(colorIndex) + "; -fx-padding: 5;");
//            FontAwesomeIconView icon = new FontAwesomeIconView();
//            icon.setGlyphSize(20);
//            icon.setGlyphName("USER");
//            label.setGraphic(icon);
//            hbox.getChildren().add(label);
//            colorIndex++;
    }


    // For Month Tab
    private void initializeMonthView() {

    }

    // For List All Tab
    private void initializeListAllView() {

    }


    private void initializeColumnsAndData() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        employees = getEmployeesFromDatabase();
        for (Employee employee : employees) {
            TableColumn<TimeSlot, Label> employeeColumn = new TableColumn<>(employee.getName());
            employeeColumn.setCellValueFactory(cellData -> {
                TimeSlot timeSlot = cellData.getValue();
                return new SimpleObjectProperty<>(timeSlot.getAppointmentDetails(employee.getName()));
            });

            employeeColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Label item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setGraphic(item);
                    }
                }
            });

            appointmentTable.getColumns().add(employeeColumn);
        }

        // Slots from 8am to 22pm
        for (int hour = 8; hour <= 21; hour++) {
            // Interval of 15 minutes
            for (int minute = 0; minute < 60; minute += 15) {
                LocalTime time = LocalTime.of(hour, minute);
                timeSlots.add(new TimeSlot(time.format(DateTimeFormatter.ofPattern("HH:mm"))));
            }
        } // set all timeSlots into table
        appointmentTable.setItems(timeSlots);
    }

    // To Create all: label, info in AppointmentColumn
    private void onDateSelected() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            String formattedDate = selectedDate.toString();
            List<Appointment> appointments = Appointment.getAppointmentForDate(formattedDate);
            populateAppointments(appointments);
        }
    }


    private void populateAppointments(List<Appointment> appointments) {
        // Clear current appointment details
        for (TimeSlot slot : timeSlots) {
            slot.appointmentDetails.clear();
        }

        // Add appointments to the corresponding time slots
        for (Appointment appointment : appointments) {
            String appointmentTime = appointment.getHour();  // startTime in db already in String
            LocalTime startTime = LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm"));
            // Get duration of appointments
            Service theService = Appointment.getServiceFromID(appointment.getServiceId());
            int durationMinutes = theService.getDuration();

            // To get the index of the timeColumn, where the appointment should start
            int startIndex = (startTime.getHour() - 8) * 4 + (startTime.getMinute() / 15);
            int span = durationMinutes / 15; // to know how many slots needed

            if (startIndex >= 0 && startIndex + span <= timeSlots.size()) {
                Client client = Client.getClientFromID(appointment.getClientId());
                Service service = Appointment.getServiceFromID(appointment.getServiceId());
                String appointmentText = client.getName() + "\n" + service.getName();
                String backgroundColor = getColorForEmployee(appointment.getStaffId());
                String employeeName = getEmployeeFromID(appointment.getStaffId()).getName();

                // style first slot with text
//                TimeSlot slot = timeSlots.get(startIndex);
//                Label label = new Label("");
//                label.setText(appointmentText);
//                label.setStyle("-fx-background-color: " + backgroundColor + ";");
//                slot.setAppointmentDetails(employeeName, label);

                // style labels without text
                for (int i = 0; i < span; i++) {
                    int index = startIndex + i;
                    if (index < timeSlots.size()) {
                        TimeSlot slot = timeSlots.get(index);
                        Label label = slot.getAppointmentDetails(employeeName);
                        if (label == null) {
                            label = new Label();
                            if (i == 0) {
                                label.setText(appointmentText);
                            } else {
                                label.setText("");
                            }
                            label.setStyle("-fx-background-color: " + backgroundColor + ";");
                            slot.setAppointmentDetails(employeeName, label);
                        }
                    }
                }
            }
        }
        appointmentTable.refresh();
    }


    // To get the label color of the Employee doing that service
    private String getColorForEmployee(int employeeId) {
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
        private final Map<String, Label> appointmentDetails;

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

    private void isPaidColor(Appointment appointment, Label lbl){
        String paid = appointment.getStatus();
        // Add green color to listview value
        if(paid.equals("Paid")) {
            lbl.setStyle("-fx-background-color: rgba(0,110,0,0.55)");
        } else {
            lbl.setStyle("-fx-background-color: rgba(115,3,3,0.49)");
        }
    }
}
