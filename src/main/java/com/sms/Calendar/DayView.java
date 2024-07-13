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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DayView {
    AppointmentController appointmentController;

    EmployeeDAO employeeDAO = new EmpDAOImplement();
    AppointmentDAO appointmentDAO = new AptDAOImplement();
    ServiceDAO serviceDAO = new ServiceDAOImplement();
    ClientDAO clientDAO = new ClientDAOImplement();


    private final ObservableList<AppointmentController.TimeSlot> timeSlots = FXCollections.observableArrayList();

    public DayView(AppointmentController appointmentController){
        this.appointmentController = appointmentController;
    }

    public void initializeDayView() throws SQLException {
        // Create all columns and add the hours
        initializeColumnsAndData();
        // set initial date in DatPicker
        appointmentController.datePicker.setValue(LocalDate.now());
        // load appointments for initial date
        onDateSelected();
        // add listener to DatePicker
        appointmentController.datePicker.setOnAction(event -> {
            try {
                onDateSelected();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initializeColumnsAndData() throws SQLException {
        appointmentController.timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        appointmentController.employees = employeeDAO.getAll();

        for (Employee employee : appointmentController.employees) {
            TableColumn<AppointmentController.TimeSlot, Label> employeeColumn = new TableColumn<>(employee.getName());
            employeeColumn.setPrefWidth(180);
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

            appointmentController.appointmentTable.getColumns().add(employeeColumn);
        }

        // Slots from 8am to 22pm
        for (int hour = 8; hour <= 21; hour++) {
            // Interval of 15 minutes
            for (int minute = 0; minute < 60; minute += 15) {
                LocalTime time = LocalTime.of(hour, minute);
                timeSlots.add(new AppointmentController.TimeSlot(time.format(DateTimeFormatter.ofPattern("HH:mm"))));
            }
        } // set all timeSlots into table
        appointmentController.appointmentTable.setItems(timeSlots);
    }

    // To Create all: label, info in AppointmentColumn
    public void onDateSelected() throws SQLException {
        LocalDate selectedDate = appointmentController.datePicker.getValue();
        if (selectedDate != null) {
            String formattedDate = selectedDate.toString();
            List<Appointment> appointments;
            appointments = appointmentDAO.getFromDate(formattedDate);
            populateAppointments(appointments);
        }
    }


    public void populateAppointments(List<Appointment> appointments) throws SQLException {
        // Clear current appointment details
        for (AppointmentController.TimeSlot slot : timeSlots) {
            slot.appointmentDetails.clear();
        }

        // Add appointments to the corresponding time slots
        for (Appointment appointment : appointments) {
            String appointmentTime = appointment.getHour();  // startTime in db already in String
            LocalTime startTime = LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm"));
            // Get duration of appointments
            Service theService = serviceDAO.get(appointment.getServiceId());
            if (theService == null) {
                System.err.println("Service not found for ID: " + appointment.getServiceId());
                continue;
            }
            int durationMinutes = theService.getDuration();

            // To get the index of the timeColumn, where the appointment should start
            int startIndex = (startTime.getHour() - 8) * 4 + (startTime.getMinute() / 15);
            int span = durationMinutes / 15; // to know how many slots needed

            if (startIndex >= 0 && startIndex + span <= timeSlots.size()) {
                Client client = clientDAO.get(appointment.getClientId());
                if (client == null) {
                    System.err.println("Client not found for ID: " + appointment.getClientId());
                    continue;
                }
                Service service = serviceDAO.get(appointment.getServiceId());
                if (service == null) {
                    System.err.println("Service not found for ID: " + appointment.getServiceId());
                    continue;
                }
                String appointmentText = client.getName() + "\n" + service.getName();
                String backgroundColor = AppointmentController.getColorForEmployee(appointment.getStaffId());
                Employee employee = employeeDAO.get(appointment.getStaffId());
                if (employee == null) {
                    System.err.println("Employee not found for ID: " + appointment.getStaffId());
                    continue;
                }
                String employeeName = employee.getName();

                for (int i = 0; i < span; i++) {
                    int index = startIndex + i;
                    if (index < timeSlots.size()) {
                        AppointmentController.TimeSlot slot = timeSlots.get(index);
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
        appointmentController.appointmentTable.refresh();
    }
}
