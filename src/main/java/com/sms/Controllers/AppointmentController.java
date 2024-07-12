package com.sms.Controllers;

import com.sms.Calendar.DayView;
import com.sms.DAO.*;

import com.sms.BackEnd.Client;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import com.sms.BackEnd.*;
import javafx.scene.layout.Pane;


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
    public ChoiceBox<String> choiceBoxClient;
    public ChoiceBox<String> hourBox;
    public ChoiceBox<String> minuteBox;
    public ChoiceBox<String> choiceBoxService;
    public ChoiceBox<String> choiceBoxEmployee;
    public DatePicker datePickerAddAppointment;
    public Button addClientBtn;
    public Pane dialogAdd;
    public Button addAppoitnmentBtn;
    public Button cancelBtn;

    List<Client> clients = new ArrayList<>();
    List<Service> services = new ArrayList<>();
    ClientDAO clientDAO = new ClientDAOImplement();
    ServiceDAO serviceDAO = new ServiceDAOImplement();
    EmployeeDAO employeeDAO = new EmpDAOImplement();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DayView dayView = new DayView(this);
            dayView.initializeDayView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add clients into add appointment choiceBox
        add.setOnAction(event -> {
            try {
                showAddButtonDialog();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        choiceBoxService.setOnAction(event ->{
            try{
                String serviceSelected = choiceBoxService.getValue();
                if (serviceSelected != null && !serviceSelected.equals("Choose service")) {
                    populateEmployeeChoiceBox(serviceSelected);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        });

        cancelBtn.setOnAction(event -> {dialogAdd.setVisible(false);});




//        try {
//            WeekView weekView = new WeekView(this);
//            weekView.initializeWeekView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            MonthView monthView = new MonthView(this);
//            monthView.initializeMonthView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            ListAllView listAllView = new ListAllView(this);
//            listAllView.initializeListAllView();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }


    // Event when add Button is clicked (set visible the dialog, and all clients to choicebox)
    public void showAddButtonDialog() throws SQLException {
        dialogAdd.setVisible(true);
        choiceBoxClient.getItems().clear();
        choiceBoxService.getItems().clear();
        choiceBoxEmployee.getItems().clear();
        hourBox.getItems().clear();
        minuteBox.getItems().clear();
        populateClientChoiceBox();
        populateServiceChoiceBox();
        addTimesChoiceBox();
        datePickerAddAppointment.setValue(LocalDate.now());
        choiceBoxEmployee.setValue("Choose Employee"); // Set default value for choiceBoxEmployee

    }

    // To add strings inside all choiceBox (except times)
    public void populateClientChoiceBox() throws SQLException {
        // add clients choicebox
        clients = clientDAO.getAll();
        if(!clients.isEmpty()){
            for (Client client : clients) {
                choiceBoxClient.getItems().add(client.getName());
            }
            choiceBoxClient.setValue("Choose existent client");
        } else {
            choiceBoxClient.setValue("No clients");
        }
    }

    public void populateServiceChoiceBox() throws SQLException {
        // add services choices into choicebox
        // TODO: Make it to appear in groups (Massage, Treatment, Beauty)
        // TODO: Make it like a search box and in groups
        services = serviceDAO.getAll();
        choiceBoxService.setValue("Choose service");
        if(!services.isEmpty()){
            for (Service service : services) {
                choiceBoxService.getItems().add(service.getName());
            }
            choiceBoxService.setValue("Choose service");
        } else {
            choiceBoxService.setValue("No services");
        }
    }

    public void populateEmployeeChoiceBox(String serviceSelected) throws SQLException {
        // add employee choices on choicebox depending on service selected
        choiceBoxEmployee.getItems().clear();
        Service theService = serviceDAO.getServiceByName(serviceSelected);
        String serviceType = theService.getServiceType();

        switch (serviceType) {
            case "Massage" -> employees = employeeDAO.getAll();
            case "Treatment", "Beauty" -> {
                employees = employeeDAO.getByType("Beautician");
                employees.addAll(employeeDAO.getByType("Supervisor"));
            }
            case "Physio" -> employees = employeeDAO.getByType("Physiotherapist");
        }
        for (Employee employee : employees) {
            choiceBoxEmployee.getItems().add(employee.getName());
        }
        choiceBoxEmployee.setValue("Choose Employee");
    }


    public void addTimesChoiceBox() throws SQLException {
        for (int hour = 8; hour <= 21; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            hourBox.getItems().add(time.format(DateTimeFormatter.ofPattern("HH")));
        }
        for (int minute = 0; minute < 60; minute += 15) {
            minuteBox.getItems().add(String.valueOf(minute));
        }
        hourBox.setValue("08");
        minuteBox.setValue("00");
    }


    // To get the label color of the Employee doing that service
    public static String getColorForEmployee(int employeeId) {
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