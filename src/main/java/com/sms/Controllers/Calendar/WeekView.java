package com.sms.Controllers.Calendar;

import com.sms.Controllers.AppointmentController;
import com.sms.DAO.*;
import com.sms.Models.Appointment;
import com.sms.Models.Client;
import com.sms.Models.Service;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeekView {
    private final AppointmentController apCon;
    private final AppointmentDAO appointmentDAO = new AptDAOImplement();
    private final ServiceDAO serviceDAO = new ServiceDAOImplement();
    private final ClientDAO clientDAO = new ClientDAOImplement();
    ObservableList<String> timeSlots = FXCollections.observableArrayList();

    public WeekView(AppointmentController appointmentController) {
        this.apCon = appointmentController;
    }

    public void initializeWeekView() throws SQLException {
        apCon.datePickerWeek.setValue(LocalDate.now());
        onDateSelected();
        apCon.datePickerWeek.setOnAction(event -> {
            try {
                onDateSelected();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onDateSelected() throws SQLException {
        LocalDate selectedDate = apCon.datePickerWeek.getValue();
        if (selectedDate != null) {
            clearTable();
        }
    }

    public void initializeColumns() throws SQLException {
        loadTimeColumn();
        loadDaysColumn();
    }

    private void loadTimeColumn() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int hour = 8; hour <= 21; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                LocalTime time = LocalTime.of(hour, minute);
                String timeFormatted = timeFormatter.format(time);
                timeSlots.add(timeFormatted);
            }
        }
        TableColumn<String, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));
        apCon.weekTable.getColumns().add(timeColumn);
        apCon.weekTable.setItems(timeSlots);
    }

    private void loadDaysColumn() throws SQLException {
        LocalDate selectedDate = apCon.datePickerWeek.getValue();
        if (selectedDate == null) {
            return; // If selectedDate is null, return early to avoid further errors
        }

        LocalDate startOfWeek = selectedDate.with(java.time.DayOfWeek.MONDAY);
        apCon.monthLabel.setText(selectedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE d");

        for (int day = 0; day < 7; day++) {
            LocalDate date = startOfWeek.plusDays(day);
            String dayTitle = dayFormatter.format(date);

            TableColumn<String, VBox> dayColumn = new TableColumn<>(dayTitle);
            dayColumn.setPrefWidth(150);
            populateAppointments(dayColumn, date);
            apCon.weekTable.getColumns().add(dayColumn);
        }
    }

    private void populateAppointments(TableColumn<String, VBox> dayColumn, LocalDate date) throws SQLException {
        List<Appointment> appointments = appointmentDAO.getFromDate(date.toString());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, VBox> timeSlotToVBoxMap = new HashMap<>();
        for (String timeSlot : timeSlots) {
            timeSlotToVBoxMap.put(timeSlot, new VBox());
        }

        for (Appointment appointment : appointments) {
            LocalTime startTime = LocalTime.parse(appointment.getHour(), timeFormatter);
            String timeSlot = timeFormatter.format(startTime);

            VBox vbox = timeSlotToVBoxMap.get(timeSlot);
            if (vbox != null) {
                Client client = clientDAO.get(appointment.getClientId());
                Service service = serviceDAO.get(appointment.getServiceId());
                if (client == null || service == null) continue;

                String appointmentText = service.getName();
                String backgroundColor = AppointmentController.getColorForEmployee(appointment.getStaffId());

                Label label = new Label();
                label.setText(timeSlot + " " + appointmentText);
                label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-font-weight: bold; " +
                        "-fx-text-fill: #4d4c4c; " +
                        "-fx-background-radius: 5; -fx-pref-width: 150;");

                vbox.getChildren().add(label);
            }
        }
        dayColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(timeSlotToVBoxMap.get(data.getValue())));
    }

    private void clearTable(){
        timeSlots.clear();
        apCon.weekTable.refresh();
        apCon.weekTable.getColumns().clear();
        try {
            initializeColumns();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
