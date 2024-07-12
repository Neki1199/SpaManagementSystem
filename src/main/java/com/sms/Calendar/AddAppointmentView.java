package com.sms.Calendar;

import com.sms.BackEnd.Client;
import com.sms.BackEnd.Employee;
import com.sms.BackEnd.Service;
import com.sms.Controllers.AppointmentController;
import com.sms.DAO.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddAppointmentView {

    private AppointmentController aptCon;

    List<Client> clients = new ArrayList<>();
    List<Service> services = new ArrayList<>();
    ClientDAO clientDAO = new ClientDAOImplement();
    ServiceDAO serviceDAO = new ServiceDAOImplement();
    EmployeeDAO employeeDAO = new EmpDAOImplement();

    public AddAppointmentView(AppointmentController appointmentController) {
        this.aptCon = appointmentController;
    }

    public void initializeView(){
        // Add clients into add appointment choiceBox
        aptCon.add.setOnAction(event -> {
            try {
                showAddButtonDialog();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        aptCon.choiceBoxService.setOnAction(event ->{
            try{
                String serviceSelected = aptCon.choiceBoxService.getValue();
                if (serviceSelected != null && !serviceSelected.equals("Choose service")) {
                    populateEmployeeChoiceBox(serviceSelected);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        });

        aptCon.cancelBtn.setOnAction(event -> {aptCon.dialogAdd.setVisible(false);});
    }

    // Event when add Button is clicked (set visible the dialog, and all clients to choicebox)
    public void showAddButtonDialog() throws SQLException {
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


    public void addTimesChoiceBox() throws SQLException {
        for (int hour = 8; hour <= 21; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            aptCon.hourBox.getItems().add(time.format(DateTimeFormatter.ofPattern("HH")));
        }
        for (int minute = 0; minute < 60; minute += 15) {
            aptCon.minuteBox.getItems().add(String.valueOf(minute));
        }
        aptCon.hourBox.setValue("08");
        aptCon.minuteBox.setValue("00");
    }

}
