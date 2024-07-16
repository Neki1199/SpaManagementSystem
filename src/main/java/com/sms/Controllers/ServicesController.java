package com.sms.Controllers;

import com.sms.BackEnd.Service;
import com.sms.DAO.ServiceDAO;
import com.sms.DAO.ServiceDAOImplement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ServicesController extends Node implements Initializable {
    public Button add;
    public Pane dialogAdd;
    public Button addServiceBtn;
    public Button cancelBtn;
    public Label errorLabel;
    public TextField nameField;
    public ChoiceBox<String> serviceTypeChoiceBox;
    public TextField durationField;
    public TextField costField;
    public ChoiceBox<String> staffTypeChoiceBox;
    public TextArea descriptionField;
    public ChoiceBox<String> chooseServiceType;
    public TableColumn<Service, Integer> IDColumn;
    public TableColumn<Service, String> nameColumn;
    public TableColumn<Service, String> descriptionColumn;
    public TableColumn<Service, Integer> durationColumn;
    public TableColumn<Service, Double> priceColumn;
    public TableColumn<Service, String> serviceTypeColumn;
    public TableColumn<Service, String> staffTypeColumn;
    public TableView<Service> servicesTable;

    ServiceDAO serviceDAO = new ServiceDAOImplement();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseServiceType.setValue("All Services");
        chooseServiceType.getItems().addAll("Massage", "Physio", "Beauty", "Treatment", "Spa", "All Services");

        add.setOnAction(event -> {
            try {
                showAddButtonDialog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        addServiceBtn.setOnAction(event -> {
            try {
                getServiceData();
                loadAllServices();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        cancelBtn.setOnAction(event -> {
            dialogAdd.setVisible(false);
        });

        initializeColumns();

        // what service is choosed, will appear on the table
        chooseServiceType.setOnAction(event -> {
            String serviceType = chooseServiceType.getValue();
            if(serviceType.equals("All Services")) {
                try {
                    loadAllServices();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (serviceType.equals("Massage")) {
                try {
                    loadMassages();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (serviceType.equals("Physio")) {
                try {
                    loadPhysio();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else if (serviceType.equals("Beauty")) {
                try {
                    loadBeauty();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else if (serviceType.equals("Treatment")) {
                try {
                    loadTreatments();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else if (serviceType.equals("Spa")) {
                try {
                    loadSpa();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // load services initally
        try{
            loadAllServices();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Event when add Button is clicked (set visible the dialog, and all services to choicebox)
    public void showAddButtonDialog() throws SQLException {
        errorLabel.setVisible(false);
        dialogAdd.setVisible(true);
        nameField.clear();
        serviceTypeChoiceBox.getItems().clear();
        durationField.clear();
        costField.clear();
        staffTypeChoiceBox.getItems().clear();
        populateServiceType();
        populateStaffType();
    }
    public void populateServiceType() throws SQLException {
        serviceTypeChoiceBox.getItems().addAll("Massage", "Physio", "Beauty", "Treatment", "Spa");
    }
    public void populateStaffType() throws SQLException {
        staffTypeChoiceBox.getItems().addAll("Beautician", "Physiotherapist");
    }

    // After click add service button
    public void getServiceData() throws SQLException {
        String name = nameField.getText();
        String serviceType = serviceTypeChoiceBox.getValue();
        String duration = durationField.getText();
        String cost = costField.getText();
        String staffType = staffTypeChoiceBox.getValue();
        String description = descriptionField.getText();

        if(!name.isEmpty() && !serviceType.isEmpty() && !duration.isEmpty() && !cost.isEmpty() && !staffType.isEmpty()) {
            Service service = new Service(null, name, description, Double.parseDouble(cost), serviceType, Integer.parseInt(duration), staffType);
            serviceDAO.insert(service);
            dialogAdd.setVisible(false);
        }else{
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Please fill all the fields");
        }
    }

//PropertyValueFactory is used to automatically extract the value of a
// property from a Service object and display it in a TableColumn.
    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        staffTypeColumn.setCellValueFactory(new PropertyValueFactory<>("staffType"));
    }

    private void loadAllServices() throws SQLException {
        List<Service> services = serviceDAO.getAll();
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void loadMassages() throws SQLException {
        List<Service> services = serviceDAO.getServiceByType("Massage");
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void loadSpa() throws SQLException {
        List<Service> services = serviceDAO.getServiceByType("Spa");
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void loadTreatments() throws SQLException {
        List<Service> services = serviceDAO.getServiceByType("Treatment");
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void loadBeauty() throws SQLException {
        List<Service> services = serviceDAO.getServiceByType("Beauty");
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void loadPhysio() throws SQLException {
        List<Service> services = serviceDAO.getServiceByType("Physio");
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }
}