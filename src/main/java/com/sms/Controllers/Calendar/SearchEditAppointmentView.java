package com.sms.Controllers.Calendar;

import com.sms.Controllers.AppointmentController;
import com.sms.DAO.*;
import com.sms.Models.Appointment;
import com.sms.Models.Client;
import com.sms.Models.Employee;
import com.sms.Models.Service;
import javafx.scene.control.Label;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SearchEditAppointmentView {

    private final AppointmentController aptCon;
    AppointmentDAO appointmentDAO = new AptDAOImplement();
    ClientDAO clientDAO = new ClientDAOImplement();
    ServiceDAO serviceDAO = new ServiceDAOImplement();
    EmployeeDAO employeeDAO = new EmpDAOImplement();

    public SearchEditAppointmentView(AppointmentController appointmentController) {
        this.aptCon = appointmentController;
    }

    public void initializeView() {

        aptCon.searchAppointmentBtn.setOnAction(event ->{
            try {
                searchClientAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        aptCon.edit.setOnAction(event ->{
            try {
                aptCon.errorLabel2.setVisible(false);
                editAppointment();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        aptCon.listViewClientSearch.setOnMouseClicked(event ->{
            String idDateAndService = aptCon.listViewClientSearch.getSelectionModel().getSelectedItem();
            String[] detailsAppointment = idDateAndService.split(" - ");
            aptCon.searchAppointmentField.setText(detailsAppointment[0]);
            aptCon.listViewClientSearch.setVisible(false);
            aptCon.errorLabel1.setVisible(false);
        });

        aptCon.updateAppointment.setOnAction(event -> {
            try {
                updateAppointment();
                aptCon.dayView.onDateSelected();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        aptCon.cancelBtn.setOnAction(event -> {
            resetDialog();
        });

        aptCon.deleteAppointment.setOnAction(event -> {
            try {
                int appointmentID = Integer.parseInt(aptCon.searchAppointmentField.getText());
                appointmentDAO.delete(appointmentID);
                aptCon.dayView.onDateSelected();
                resetDialog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateAppointment() throws SQLException {
        String appointmentIDStr = aptCon.searchAppointmentField.getText();
        if (appointmentIDStr.isEmpty()) {
            showError(aptCon.errorLabel, "Error: enter appointment ID");
            return;
        }

        int appointmentID = Integer.parseInt(appointmentIDStr);
        Appointment appointment = appointmentDAO.get(appointmentID);

        if (!validateInputs()) return;

        String clientSelected = aptCon.searchClientField.getText();
        String serviceSelected = aptCon.choiceBoxService.getValue();
        String employeeSelected = aptCon.choiceBoxEmployee.getValue();
        LocalDate dateNoFormat = aptCon.datePickerAddAppointment.getValue();
        String dateSelected = dateNoFormat.toString();
        int hourSelected = Integer.parseInt(aptCon.hourBox.getValue());
        int minuteSelected = Integer.parseInt(aptCon.minuteBox.getValue());

        Service service = serviceDAO.getServiceByName(serviceSelected);
        int duration = service.getDuration();

        boolean isSlotAvailable = aptCon.appointmentView.checkSlotAvailability(dateSelected, hourSelected, minuteSelected, duration, employeeSelected);
        Client newClient = clientDAO.getClientByName(clientSelected);
        if (isSlotAvailable || (appointment.getDate().equals(dateSelected) && appointment.getClientId() != newClient.getId())) {
            updateAppointmentDetails(appointment, clientSelected, serviceSelected, employeeSelected, dateSelected, hourSelected, minuteSelected);
            resetDialog();
        } else {
            showError(aptCon.errorLabel, "Error: time slot full, choose a different hour");
        }
    }

    private void editAppointment() throws SQLException {
        String appointmentIDStr = aptCon.searchAppointmentField.getText();
        if (appointmentIDStr.isEmpty()) {
            showError(aptCon.errorLabel1, "Error: enter appointment ID");
            return;
        }

        int appointmentID = Integer.parseInt(appointmentIDStr);
        Appointment appointment = appointmentDAO.get(appointmentID);

        aptCon.dialogAdd.setVisible(true);
        aptCon.addAppointmentBtn.setVisible(false);
        aptCon.boxUpdateDelete.setVisible(true);

        Client client = clientDAO.get(appointment.getClientId());
        Service service = serviceDAO.get(appointment.getServiceId());
        Employee employee = employeeDAO.get(appointment.getStaffId());

        aptCon.searchClientField.setText(client.getName());
        aptCon.serviceTypeBox.setValue(service.getServiceType());
        aptCon.choiceBoxService.setValue(service.getName());
        aptCon.choiceBoxEmployee.setValue(employee.getName());
        aptCon.datePickerAddAppointment.setValue(LocalDate.parse(appointment.getDate()));
        LocalTime hourMinute = LocalTime.parse(appointment.getHour());
        aptCon.hourBox.setValue(hourMinute.format(DateTimeFormatter.ofPattern("HH")));
        aptCon.minuteBox.setValue(hourMinute.format(DateTimeFormatter.ofPattern("mm")));
    }

    private void searchClientAppointments() throws SQLException {
        String clientName = aptCon.searchAppointmentField.getText();

        appointmentDAO.search(aptCon.listViewClientSearch, clientName);
        if (aptCon.listViewClientSearch.getItems().isEmpty()) {
            showError(aptCon.errorLabel2, "Client with 0 appointments");
            return;
        }

        aptCon.listViewClientSearch.setVisible(true);
    }

    private boolean validateInputs() {
        if (aptCon.datePickerAddAppointment.getValue() == null || aptCon.serviceTypeBox.getValue() == null || aptCon.searchClientField.getText() == null || aptCon.choiceBoxService.getValue() == null || aptCon.choiceBoxEmployee.getValue() == null) {
            showError(aptCon.errorLabel, "Error: enter all details");
            return false;
        }
        if (aptCon.serviceTypeBox.getValue().equals("Choose Service Type") || aptCon.searchClientField.getText().isEmpty() || aptCon.choiceBoxService.getValue().equals("Choose Service") || aptCon.choiceBoxEmployee.getValue().equals("Choose Employee")) {
            showError(aptCon.errorLabel, "Error: enter all details");
            return false;
        }
        return true;
    }

    private void updateAppointmentDetails(Appointment appointment, String clientSelected, String serviceSelected, String employeeSelected, String dateSelected, int hourSelected, int minuteSelected) throws SQLException {
        Client client = clientDAO.getClientByName(clientSelected);
        Employee employee = employeeDAO.getEmployeeByName(employeeSelected);
        LocalTime startTime = LocalTime.of(hourSelected, minuteSelected);
        String hour = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        appointment.setClientId(client.getId());
        appointment.setServiceId(serviceDAO.getServiceByName(serviceSelected).getId());
        appointment.setStaffId(employee.getId());
        appointment.setDate(dateSelected);
        appointment.setHour(hour);
        appointmentDAO.update(appointment);
    }

    private void resetDialog() {
        aptCon.dialogAdd.setVisible(false);
        aptCon.errorLabel.setVisible(false);
        aptCon.errorLabel1.setVisible(false);
        aptCon.errorLabel2.setVisible(false);
        aptCon.searchAppointmentField.clear();
        aptCon.addAppointmentBtn.setVisible(true);
        aptCon.boxUpdateDelete.setVisible(false);
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
}
