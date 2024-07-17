package com.sms.Controllers.Calendar;

import com.sms.Models.Appointment;
import com.sms.Models.Client;
import com.sms.Models.Employee;
import com.sms.Models.Service;
import com.sms.Controllers.AppointmentController;
import com.sms.DAO.*;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddAppointmentView {

    private final AppointmentController aptCon;

    List<Client> clients = new ArrayList<>();
    List<Service> services = new ArrayList<>();
    final ClientDAO clientDAO = new ClientDAOImplement();
    final ServiceDAO serviceDAO = new ServiceDAOImplement();
    final EmployeeDAO employeeDAO = new EmpDAOImplement();
    final AppointmentDAO appointmentDAO = new AptDAOImplement();

    public AddAppointmentView(AppointmentController appointmentController) {
        this.aptCon = appointmentController;
    }

    public void initializeView() {
        // Add clients into add appointment choiceBox
        aptCon.add.setOnAction(event -> {
            try {
                showAddButtonDialog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        aptCon.choiceBoxService.setOnAction(event ->{
            try{
                String serviceSelected = aptCon.choiceBoxService.getValue();
                if (serviceSelected != null && !serviceSelected.equals("Choose service")) {
                    populateEmployeeChoiceBox(serviceSelected);
                }
            }catch(SQLException e){
                throw new RuntimeException(e);            }
        });

        aptCon.addAppoitnmentBtn.setOnAction(event ->{
            try{
                getAllData();
                aptCon.dayView.onDateSelected();
            } catch(SQLException e){
                throw new RuntimeException(e);            }
        });

        aptCon.datePickerAddAppointment.setOnAction(event -> {
            try {
                LocalDate selectedDate = aptCon.datePickerAddAppointment.getValue();
                if (selectedDate != null) {
                    String formattedDate = selectedDate.toString();
                    List<Appointment> appointments;
                    appointments = appointmentDAO.getFromDate(formattedDate);
                    aptCon.dayView.populateAppointments(appointments);
                }
                aptCon.datePicker.setValue(selectedDate);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });

        aptCon.cancelBtn.setOnAction(event -> aptCon.dialogAdd.setVisible(false));
    }

    // Event when add Button is clicked (set visible the dialog, and all clients to choicebox)
    public void showAddButtonDialog() throws SQLException {
        aptCon.errorLabel.setVisible(false);
        aptCon.dialogAdd.setVisible(true);
        aptCon.choiceBoxClient.getItems().clear();
        aptCon.choiceBoxService.getItems().clear();
        aptCon.choiceBoxEmployee.getItems().clear();
        aptCon.hourBox.getItems().clear();
        aptCon.minuteBox.getItems().clear();
        populateClientChoiceBox();
        populateServiceChoiceBox();
        addTimesChoiceBox();
        aptCon.datePickerAddAppointment.setValue(LocalDate.now());
        aptCon.choiceBoxEmployee.setValue("Choose Employee"); // Set default value for choiceBoxEmployee

    }

    // To add strings inside all choiceBox (except times)
    public void populateClientChoiceBox() throws SQLException {
        // add clients choicebox
        clients = clientDAO.getAll();
        if(!clients.isEmpty()){
            for (Client client : clients) {
                aptCon.choiceBoxClient.getItems().add(client.getName());
            }
            aptCon.choiceBoxClient.setValue("Choose existent client");
        } else {
            aptCon.choiceBoxClient.setValue("No clients");
        }
    }

    public void populateServiceChoiceBox() throws SQLException {
        // add services choices into choicebox
        // TODO: Make it to appear in groups (Massage, Treatment, Beauty)
        // TODO: Make it like a search box and in groups
        services = serviceDAO.getAll();
        aptCon.choiceBoxService.setValue("Choose service");
        if(!services.isEmpty()){
            for (Service service : services) {
                aptCon.choiceBoxService.getItems().add(service.getName());
            }
            aptCon.choiceBoxService.setValue("Choose service");
        } else {
            aptCon.choiceBoxService.setValue("No services");
        }
    }

    public void populateEmployeeChoiceBox(String serviceSelected) throws SQLException {
        // add employee choices on choicebox depending on service selected
        aptCon.choiceBoxEmployee.getItems().clear();
        Service theService = serviceDAO.getServiceByName(serviceSelected);
        String serviceType = theService.getServiceType();

        switch (serviceType) {
            case "Massage" -> aptCon.employees = employeeDAO.getAll();
            case "Treatment", "Beauty" -> {
                aptCon.employees = employeeDAO.getByType("Beautician");
                aptCon.employees.addAll(employeeDAO.getByType("Supervisor"));
            }
            case "Physio" -> aptCon.employees = employeeDAO.getByType("Physiotherapist");
        }
        for (Employee employee : aptCon.employees) {
            aptCon.choiceBoxEmployee.getItems().add(employee.getName());
        }
        aptCon.choiceBoxEmployee.setValue("Choose Employee");
    }


    public void addTimesChoiceBox() {
        for (int hour = 8; hour <= 21; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            aptCon.hourBox.getItems().add(time.format(DateTimeFormatter.ofPattern("HH")));
        }
        for (int minute = 0; minute < 60; minute += 15) {
            LocalTime time = LocalTime.of(0, minute);
            aptCon.minuteBox.getItems().add(time.format(DateTimeFormatter.ofPattern("mm")));
        }
        aptCon.hourBox.setValue("08");
        aptCon.minuteBox.setValue("00");
    }

    public void getAllData() throws SQLException {
        String clientSelected = aptCon.choiceBoxClient.getValue();
        String serviceSelected = aptCon.choiceBoxService.getValue();
        String employeeSelected = aptCon.choiceBoxEmployee.getValue();
        LocalDate dateNoFormat = aptCon.datePickerAddAppointment.getValue();
        String dateSelected = dateNoFormat.toString();
        String hourNoFormat = aptCon.hourBox.getValue();
        int hourSelected = Integer.parseInt(aptCon.hourBox.getValue());
        int minuteSelected = Integer.parseInt(aptCon.minuteBox.getValue());

        Service service = serviceDAO.getServiceByName(serviceSelected);
        int duration = service.getDuration();
        boolean isSlotAvailable = true;

        // to check that an appointment does not exist already at that time, duration
        LocalTime startTime = LocalTime.of(hourSelected, minuteSelected);
        LocalTime endTime = startTime.plusMinutes(duration);
        List<Appointment> appointments = appointmentDAO.getFromDate(dateSelected);
        for (Appointment apt : appointments) {
            int serviceId = apt.getServiceId();
            Service theService = serviceDAO.get(serviceId);
            LocalTime aptStartTime = LocalTime.parse(apt.getHour(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime aptEndTime = aptStartTime.plusMinutes(theService.getDuration());

            // Check if there is any overlap
            if (isOverlap(startTime, endTime, aptStartTime, aptEndTime)) {
                isSlotAvailable = false;
                break; // No need to continue checking
            }
        }

        if (serviceSelected != null && !serviceSelected.equals("Choose service") && employeeSelected != null && !employeeSelected.equals("Choose employee") && !dateSelected.equals("Choose date") && isSlotAvailable){
            Client client = clientDAO.getClientByName(clientSelected);
            Employee employee = employeeDAO.getEmployeeByName(employeeSelected);
            String hour = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            Appointment newAppointment = new Appointment(null, client.getId(), service.getId(), employee.getId(), dateSelected, hour, "Not Paid");
            appointmentDAO.insert(newAppointment);
            aptCon.dialogAdd.setVisible(false);
            aptCon.appointmentTable.refresh();
        } else if (!isSlotAvailable) {
            aptCon.errorLabel.setText("Error: time slot full, choose a different hour");
            aptCon.errorLabel.setVisible(true);
        }else{
            aptCon.errorLabel.setText("Error: enter all details");
            aptCon.errorLabel.setVisible(true);
        }
    }
    private boolean isOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
