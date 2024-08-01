package com.sms.Controllers;

import com.sms.DAO.*;
import com.sms.Models.Appointment;
import com.sms.Models.Client;
import com.sms.Models.Employee;
import com.sms.Models.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.sms.Controllers.NotifyAppointmentsChanges.appointmentCountProperty;

public class PaymentsController implements Initializable {
    public Label todayDateLbl;
    public Text noAppointmentText;
    public BorderPane paymentPane;
    public TableView<Appointment> paymentTable;
    public TableColumn<Appointment, Integer> IDColumn;
    public TableColumn<Appointment, String> nameColumn;
    public TableColumn<Appointment, String> employeeColumn;
    public TableColumn<Appointment, String> serviceColumn;

    private final AppointmentDAO appointmentDAO = new AptDAOImplement();
    private final ClientDAO clientDAO = new ClientDAOImplement();
    private final EmployeeDAO employeeDAO = new EmpDAOImplement();
    private final ServiceDAO serviceDAO = new ServiceDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noAppointmentText.setVisible(false);
        paymentPane.setVisible(false);

        LocalDate today = LocalDate.now();
        todayDateLbl.setText(today.toString());

        try {
            initializeColumns(today);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // when the NotifyChangesAppointments get a change, update this ui
        appointmentCountProperty().addListener((obs, oldCount, newCount) -> {
            if (newCount != null) {
                int count = newCount.intValue();
                if (count > 0) {
                    try {
                        initializeColumns(today);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        paymentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                paymentPane.setVisible(true);
            }
        });
    }

    public void initializeColumns(LocalDate today) throws SQLException {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        // Get the names by using nested property value factories
        nameColumn.setCellValueFactory(cellData -> {
            try {
                Client client = clientDAO.get(cellData.getValue().getClientId());
                return new SimpleStringProperty(client.getName());
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown");
            }
        });
        employeeColumn.setCellValueFactory(cellData -> {
            try {
                Employee employee = employeeDAO.get(cellData.getValue().getStaffId());
                return new SimpleStringProperty(employee.getName());
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown");
            }
        });
        serviceColumn.setCellValueFactory(cellData -> {
            try {
                Service service = serviceDAO.get(cellData.getValue().getServiceId());
                return new SimpleStringProperty(service.getName());
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown");
            }
        });

        loadAppointments(today);
    }

    public void loadAppointments(LocalDate today) throws SQLException {
        List<Appointment> appointments = getTodayAppointments(today);
        paymentTable.getItems().clear();

        // get appointment with status not paid
        if(appointments.isEmpty()){
            noAppointmentText.setVisible(true);
        } else {
            List<Appointment> notPaidAppointments = appointmentDAO.getNotPaid(appointments);
            if(notPaidAppointments.isEmpty()){
                noAppointmentText.setVisible(true);
            } else {
                ObservableList<Appointment> notPaidAppointmentsList = FXCollections.observableArrayList(notPaidAppointments);
                paymentTable.setItems(notPaidAppointmentsList);
            }
        }
    }

    // show appointments for current day that has "Not Paid" status
    private List<Appointment> getTodayAppointments(LocalDate today) throws SQLException {
        return appointmentDAO.getFromDate(String.valueOf(today));
    }

    // check if the client has more appointments the same day
    private List<Appointment> checkClientMoreThanOneAppointment(Integer clientID, List<Appointment> appointmentsToPay) throws SQLException {
        List<Appointment> allAppointments = new ArrayList<>();
        for(Appointment appointment: appointmentsToPay){
            if(clientID.equals(appointment.getClientId())){
                allAppointments.add(appointment);
            }
        }
        return allAppointments;
    }

    // create only one payment process for all the client appointments (for that day)

}
