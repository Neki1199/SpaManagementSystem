package com.sms.Controllers.Calendar;

import com.sms.Controllers.ClientsController;
import com.sms.Models.Appointment;
import com.sms.Models.Client;
import com.sms.Models.Employee;
import com.sms.Models.Service;
import com.sms.Controllers.AppointmentController;
import com.sms.DAO.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddAppointmentView {

    private final AppointmentController aptCon;
    private final ClientDAO clientDAO = new ClientDAOImplement();
    private final ServiceDAO serviceDAO = new ServiceDAOImplement();
    private final EmployeeDAO employeeDAO = new EmpDAOImplement();
    private final AppointmentDAO appointmentDAO = new AptDAOImplement();
    private List<Service> services = new ArrayList<>();

    public AddAppointmentView(AppointmentController appointmentController) {
        this.aptCon = appointmentController;
    }

    public void initializeView() {
        aptCon.add.setOnAction(event -> showAddButtonDialog());
        aptCon.searchClientBtn.setOnAction(event -> searchClient());
        aptCon.addClientBtn.setOnAction(event -> aptCon.addClientGrid.setVisible(true));
        aptCon.addClientBtn1.setOnAction(event -> addNewClient());
        aptCon.cancelBtn1.setOnAction(event -> cancelAddClient());
        aptCon.clientListView.setOnMouseClicked(event -> selectClientFromList());
        aptCon.serviceTypeBox.setOnAction(event -> updateServiceChoiceBox());
        aptCon.choiceBoxService.setOnAction(event -> updateEmployeeChoiceBox());
        aptCon.addAppointmentBtn.setOnAction(event -> addAppointment());
        aptCon.datePickerAddAppointment.setOnAction(event -> updateAppointmentsForDate());
        aptCon.cancelBtn.setOnAction(event -> {
            aptCon.clientListView.setVisible(false);
            aptCon.errorLabel.setVisible(false);
            aptCon.dialogAdd.setVisible(true);
        });
    }

    private void showAddButtonDialog() {
        try {
            aptCon.searchClientField.clear();
            aptCon.clientListView.setVisible(false);
            aptCon.errorLabel.setVisible(false);
            aptCon.dialogAdd.setVisible(true);
            aptCon.choiceBoxService.getItems().clear();
            aptCon.serviceTypeBox.getItems().clear();
            aptCon.choiceBoxEmployee.getItems().clear();
            aptCon.hourBox.getItems().clear();
            aptCon.minuteBox.getItems().clear();
            populateServiceTypeBox();
            populateTimeChoiceBoxes();
            aptCon.datePickerAddAppointment.setValue(LocalDate.now());
            aptCon.choiceBoxEmployee.setValue("Choose Employee");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateServiceTypeBox() throws SQLException {
        services = serviceDAO.getAll();
        aptCon.serviceTypeBox.setValue("Choose Service Type");
        aptCon.choiceBoxService.setValue("Choose Service");
        Set<String> serviceTypes = new HashSet<>();
        for (Service service : services) {
            if (!"Spa".equals(service.getServiceType())) {
                serviceTypes.add(service.getServiceType());
            }
        }
        aptCon.serviceTypeBox.getItems().addAll(new ArrayList<>(serviceTypes));
    }

    private void populateTimeChoiceBoxes() {
        for (int hour = 8; hour <= 21; hour++) {
            aptCon.hourBox.getItems().add(LocalTime.of(hour, 0).format(DateTimeFormatter.ofPattern("HH")));
        }
        for (int minute = 0; minute < 60; minute += 15) {
            aptCon.minuteBox.getItems().add(LocalTime.of(0, minute).format(DateTimeFormatter.ofPattern("mm")));
        }
        aptCon.hourBox.setValue("08");
        aptCon.minuteBox.setValue("00");
    }

    private void updateServiceChoiceBox() {
        try {
            String serviceTypeSelected = aptCon.serviceTypeBox.getValue();
            if (serviceTypeSelected != null && !serviceTypeSelected.equals("Choose Service Type")) {
                aptCon.choiceBoxService.getItems().clear();
                services = serviceDAO.getServiceByType(serviceTypeSelected);
                for (Service service : services) {
                    aptCon.choiceBoxService.getItems().add(service.getName());
                }
                aptCon.choiceBoxService.setValue("Choose Service");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEmployeeChoiceBox() {
        try {
            String serviceSelected = aptCon.choiceBoxService.getValue();
            if (serviceSelected != null && !serviceSelected.equals("Choose Service")) {
                aptCon.choiceBoxEmployee.getItems().clear();
                Service service = serviceDAO.getServiceByName(serviceSelected);
                String serviceType = service.getServiceType();
                aptCon.employees = switch (serviceType) {
                    case "Massage" -> employeeDAO.getAll();
                    case "Treatment", "Beauty" -> {
                        List<Employee> employees = employeeDAO.getByType("Beautician");
                        employees.addAll(employeeDAO.getByType("Supervisor"));
                        yield employees;
                    }
                    case "Physio" -> employeeDAO.getByType("Physiotherapist");
                    default -> new ArrayList<>();
                };
                for (Employee employee : aptCon.employees) {
                    aptCon.choiceBoxEmployee.getItems().add(employee.getName());
                }
                aptCon.choiceBoxEmployee.setValue("Choose Employee");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addAppointment() {
        try {
            String clientSelected = aptCon.searchClientField.getText();
            String serviceSelected = aptCon.choiceBoxService.getValue();
            String serviceTypeSelected = aptCon.serviceTypeBox.getValue();
            String employeeSelected = aptCon.choiceBoxEmployee.getValue();
            LocalDate dateNoFormat = aptCon.datePickerAddAppointment.getValue();

            if (isInvalidAppointmentData(clientSelected, serviceSelected, serviceTypeSelected, employeeSelected, dateNoFormat)) {
                aptCon.errorLabel.setText("Error: enter all details");
                aptCon.errorLabel.setVisible(true);
                return;
            }

            String dateSelected = dateNoFormat.toString();
            int hourSelected = Integer.parseInt(aptCon.hourBox.getValue());
            int minuteSelected = Integer.parseInt(aptCon.minuteBox.getValue());
            Service service = serviceDAO.getServiceByName(serviceSelected);
            int duration = service.getDuration();

            if (checkSlotAvailability(dateSelected, hourSelected, minuteSelected, duration, employeeSelected)) {
                Client client = clientDAO.getClientByName(clientSelected);
                Employee employee = employeeDAO.getEmployeeByName(employeeSelected);
                LocalTime startTime = LocalTime.of(hourSelected, minuteSelected);
                String hour = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                Appointment newAppointment = new Appointment(null, client.getId(), service.getId(), employee.getId(), dateSelected, hour, "Not Paid");
                appointmentDAO.insert(newAppointment);

                resetAddAppointmentDialog();
                aptCon.dayView.onDateSelected();
            } else {
                aptCon.errorLabel.setText("Error: time slot full, choose a different hour");
                aptCon.errorLabel.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isInvalidAppointmentData(String clientSelected, String serviceSelected, String serviceTypeSelected, String employeeSelected, LocalDate dateNoFormat) {
        return dateNoFormat == null || serviceTypeSelected == null || clientSelected == null || serviceSelected == null || employeeSelected == null ||
                serviceTypeSelected.equals("Choose Service Type") || clientSelected.isEmpty() || serviceSelected.equals("Choose Service") || employeeSelected.equals("Choose Employee");
    }

    public boolean checkSlotAvailability(String dateSelected, int hourSelected, int minuteSelected, int duration, String employeeSelected) throws SQLException {
        LocalTime startTime = LocalTime.of(hourSelected, minuteSelected);
        LocalTime endTime = startTime.plusMinutes(duration);
        List<Appointment> appointments = appointmentDAO.getFromDate(dateSelected);
        int employeeIDSelected = employeeDAO.getEmployeeByName(employeeSelected).getId();

        for (Appointment apt : appointments) {
            int employeeApt = apt.getStaffId();
            Service theService = serviceDAO.get(apt.getServiceId());
            LocalTime aptStartTime = LocalTime.parse(apt.getHour(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime aptEndTime = aptStartTime.plusMinutes(theService.getDuration());

            if (employeeApt == employeeIDSelected && isOverlap(startTime, endTime, aptStartTime, aptEndTime)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        return startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);
    }

    private void resetAddAppointmentDialog() {
        aptCon.dialogAdd.setVisible(false);
        aptCon.searchClientField.clear();
        aptCon.errorLabel.setVisible(false);
    }

    private void searchClient() {
        try {
            String clientName = aptCon.searchClientField.getText();
            clientDAO.search(aptCon.clientListView, clientName);
            if (aptCon.clientListView.getItems().isEmpty()) {
                aptCon.clientListView.setVisible(false);
                aptCon.errorLabel.setText("Client not found");
                aptCon.errorLabel.setVisible(true);
            } else {
                aptCon.clientListView.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNewClient() {
        try {
            ClientsController clientsController = new ClientsController();
            String name = aptCon.nameField.getText();
            String email = aptCon.emailField.getText();
            String phone = aptCon.phoneField.getText();
            String notes = aptCon.notesField.getText();
            aptCon.searchClientField.setText(name);

            if (clientsController.checkClientExists(name, phone, email)) {
                aptCon.errorLabel1.setText("Error: Client already exists");
                aptCon.errorLabel1.setVisible(true);
            } else if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                aptCon.errorLabel1.setText("Error: Please fill all the fields");
                aptCon.errorLabel1.setVisible(true);
            } else {
                Client client = new Client(null, name, phone, notes, email);
                clientDAO.insert(client);
                aptCon.addClientGrid.setVisible(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void cancelAddClient() {
        aptCon.errorLabel1.setVisible(false);
        aptCon.nameField.clear();
        aptCon.notesField.clear();
        aptCon.emailField.clear();
        aptCon.phoneField.clear();
        aptCon.addClientGrid.setVisible(false);
    }

    private void selectClientFromList() {
        String nameAndPhoneClient = aptCon.clientListView.getSelectionModel().getSelectedItem();
        String[] clientName = nameAndPhoneClient.split("-");
        aptCon.searchClientField.setText(clientName[0]);
        aptCon.clientListView.setVisible(false);
        aptCon.errorLabel.setVisible(false);
    }

    private void updateAppointmentsForDate() {
        try {
            LocalDate selectedDate = aptCon.datePickerAddAppointment.getValue();
            if (selectedDate != null) {
                List<Appointment> appointments = appointmentDAO.getFromDate(selectedDate.toString());
                aptCon.dayView.populateAppointments(appointments);
            }
            aptCon.datePicker.setValue(selectedDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
