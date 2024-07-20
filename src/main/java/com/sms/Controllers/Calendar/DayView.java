package com.sms.Controllers.Calendar;

import com.sms.Models.*;
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
    private final AppointmentController aptController;
    private final EmployeeDAO employeeDAO = new EmpDAOImplement();
    private final AppointmentDAO appointmentDAO = new AptDAOImplement();
    private final ServiceDAO serviceDAO = new ServiceDAOImplement();
    private final ClientDAO clientDAO = new ClientDAOImplement();
    private final ObservableList<AppointmentController.TimeSlot> timeSlots = FXCollections.observableArrayList();

    public DayView(AppointmentController appointmentController) {
        this.aptController = appointmentController;
    }

    public void initializeDayView() throws SQLException {
        initializeColumnsAndData();
        aptController.datePicker.setValue(LocalDate.now());
        onDateSelected();
        aptController.datePicker.setOnAction(event -> {
            try {
                onDateSelected();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeColumnsAndData() throws SQLException {
        setupTimeColumn();
        loadEmployeeColumns();
        setupTimeSlots();
        aptController.appointmentTable.setItems(timeSlots);
    }

    private void setupTimeColumn() {
        aptController.timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    private void loadEmployeeColumns() throws SQLException {
        aptController.employees = employeeDAO.getAll();
        for (Employee employee : aptController.employees) {
            TableColumn<AppointmentController.TimeSlot, Label> employeeColumn = createEmployeeColumn(employee);
            aptController.appointmentTable.getColumns().add(employeeColumn);
        }
    }

    private TableColumn<AppointmentController.TimeSlot, Label> createEmployeeColumn(Employee employee) {
        TableColumn<AppointmentController.TimeSlot, Label> employeeColumn = new TableColumn<>(employee.getName());
        employeeColumn.setPrefWidth(170);
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

        return employeeColumn;
    }

    private void setupTimeSlots() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int hour = 8; hour <= 21; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                LocalTime time = LocalTime.of(hour, minute);
                timeSlots.add(new AppointmentController.TimeSlot(time.format(timeFormatter)));
            }
        }
    }

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

    public void populateAppointments(List<Appointment> appointments) throws SQLException {
        clearAppointments();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Appointment appointment : appointments) {
            LocalTime startTime = LocalTime.parse(appointment.getHour(), timeFormatter);
            Service service = serviceDAO.get(appointment.getServiceId());
            if (service == null) continue;

            int durationMinutes = service.getDuration();
            int startIndex = (startTime.getHour() - 8) * 4 + (startTime.getMinute() / 15);
            int span = durationMinutes / 15;

            if (startIndex >= 0 && startIndex + span <= timeSlots.size()) {
                Client client = clientDAO.get(appointment.getClientId());
                String appointmentText = client.getName() + "\n" + service.getName();
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
                            setupLabel(label, appointment, backgroundColor, i, durationMinutes, startTime, appointmentText);
                            slot.setAppointmentDetails(employeeName, label);
                        }
                    }
                }
            }
        }
        aptController.appointmentTable.refresh();
    }

    private void clearAppointments() {
        for (AppointmentController.TimeSlot slot : timeSlots) {
            slot.appointmentDetails.clear();
        }
        aptController.appointmentTable.refresh();
    }

    private void setupLabel(Label label, Appointment appointment, String backgroundColor, int index, int durationMinutes, LocalTime startTime, String appointmentText) {
        LocalTime endTime = startTime.plus(Duration.ofMinutes(durationMinutes));
        switch (index) {
            case 0:
                label.setText(appointment.getHour() + " - " + endTime);
                label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: #4d4c4c; -fx-background-radius: 5 5 0 0;");
                break;
            case 1:
                label.setStyle("-fx-background-color: #faf0e9; -fx-font-weight: normal; -fx-font-style: italic; -fx-text-fill: #4d4c4c;");
                label.setText(appointmentText);
                break;
            default:
                label.setStyle("-fx-background-color: #FAF0E9;");
                label.setText("");
                break;
        }
    }
}
