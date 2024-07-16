package com.sms.Calendar;

import com.sms.BackEnd.*;
import com.sms.Controllers.AppointmentController;
import com.sms.DAO.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DayView {
    final AppointmentController aptController;
    final EmployeeDAO employeeDAO = new EmpDAOImplement();
    final AppointmentDAO appointmentDAO = new AptDAOImplement();
    final ServiceDAO serviceDAO = new ServiceDAOImplement();
    final ClientDAO clientDAO = new ClientDAOImplement();

    private final ObservableList<AppointmentController.TimeSlot> timeSlots = FXCollections.observableArrayList();

    public DayView(AppointmentController appointmentController){
        this.aptController = appointmentController;
    }

    public void initializeDayView() throws SQLException {
        // Create all columns and add the hours
        initializeColumnsAndData();
        // set initial date in DatPicker
        aptController.datePicker.setValue(LocalDate.now());
        // load appointments for initial date
        onDateSelected();
        // add listener to DatePicker
        aptController.datePicker.setOnAction(event -> {
            try {
                onDateSelected();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initializeColumnsAndData() throws SQLException {
        aptController.timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        aptController.employees = employeeDAO.getAll();

        for (Employee employee : aptController.employees) {
            TableColumn<AppointmentController.TimeSlot, Label> employeeColumn = new TableColumn<>(employee.getName());
            employeeColumn.setPrefWidth(170);

            // Fill each column header with color and rows with grey
            // assign a class style to the header
            String headerStyle = "column-header-employee" + employee.getId();
            String evenCell = "table-row-cell";
            employeeColumn.getStyleClass().addAll(headerStyle, evenCell);

            employeeColumn.setCellValueFactory(cellData -> {
                AppointmentController.TimeSlot timeSlot = cellData.getValue();
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
            aptController.appointmentTable.getColumns().add(employeeColumn);
        }

        // Slots from 8am to 22pm
        for (int hour = 8; hour <= 21; hour++) {
            // Interval of 15 minutes
            for (int minute = 0; minute < 60; minute += 15) {
                LocalTime time = LocalTime.of(hour, minute);
                timeSlots.add(new AppointmentController.TimeSlot(time.format(DateTimeFormatter.ofPattern("HH:mm"))));
            }
        } // set all timeSlots into table
        aptController.appointmentTable.setItems(timeSlots);
    }

    // To Create all: label, info in AppointmentColumn
    public void onDateSelected() throws SQLException {
        LocalDate selectedDate = aptController.datePicker.getValue();
        if (selectedDate != null) {
            List<Appointment> appointments = appointmentDAO.getFromDate(selectedDate.toString());
            if (appointments.isEmpty()) {
                clearAppointments();
            } else {
                populateAppointments(appointments);
            }
        }
    }

    private void clearAppointments() {
        for (AppointmentController.TimeSlot slot : timeSlots) {
            slot.appointmentDetails.clear();
        }
        aptController.appointmentTable.refresh();
    }

    public void populateAppointments(List<Appointment> appointments) throws SQLException {
        clearAppointments();

        for (Appointment appointment : appointments) {
            LocalTime startTime = LocalTime.parse(appointment.getHour(), DateTimeFormatter.ofPattern("HH:mm"));
            Service theService = serviceDAO.get(appointment.getServiceId());
            if (theService == null) continue;

            int durationMinutes = theService.getDuration();
            int startIndex = (startTime.getHour() - 8) * 4 + (startTime.getMinute() / 15);
            int span = durationMinutes / 15;

            if (startIndex >= 0 && startIndex + span <= timeSlots.size()) {
                Client client = clientDAO.get(appointment.getClientId());
                String appointmentText = client.getName() + "\n" + theService.getName();
                String backgroundColor = AppointmentController.getColorForEmployee(appointment.getStaffId());
                Employee employee = employeeDAO.get(appointment.getStaffId());
                String employeeName = employee.getName();

                for (int i = 0; i < span; i++) {
                    int index = startIndex + i;
                    if (index < timeSlots.size()) {
                        AppointmentController.TimeSlot slot = timeSlots.get(index);
                        Label label = slot.getAppointmentDetails(employeeName);
                        if (label == null) {
                            label = new Label();
                            if (i == 0) {
                                LocalTime endTime = startTime.plus(Duration.ofMinutes(durationMinutes));
                                label.setText(appointment.getHour() + " - " + endTime);
                                label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 20 20 0 0; -fx-text-fill: #4d4c4c;");
                            } else if (i == 1) {
                                label.setStyle("-fx-background-color: " + backgroundColor + ";");
                                label.setText(appointmentText);
                            } else if (i == span - 1) {
                                label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 0 0 20 20;");
                            } else {
                                label.setStyle("-fx-background-color: " + backgroundColor + ";");
                                label.setText("");
                            }
                            slot.setAppointmentDetails(employeeName, label);
                        }
                    }
                }
            }
        }
        aptController.appointmentTable.refresh();
    }
}
