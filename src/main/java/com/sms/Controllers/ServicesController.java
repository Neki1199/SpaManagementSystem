package com.sms.Controllers;

import com.sms.Models.Service;
import com.sms.DAO.ServiceDAO;
import com.sms.DAO.ServiceDAOImplement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ServicesController extends Node implements Initializable {
    public Button add, addServiceBtn, cancelBtn, searchServiceBtn, editBtn, updateServiceBtn, deleteServiceBtn;
    public Label errorLabel, errorLabelSearch;
    public TextField nameField, durationField, searchServiceField, priceField;
    public ChoiceBox<String> serviceTypeChoiceBox, staffTypeChoiceBox, chooseServiceType;
    public TextArea descriptionField;
    public TableColumn<Service, Integer> IDColumn, durationColumn, quantityColumn;
    public TableColumn<Service, String> nameColumn, descriptionColumn, serviceTypeColumn, staffTypeColumn;
    public TableColumn<Service, Double> priceColumn;
    public TableView<Service> servicesTable;
    public GridPane dialogAdd, addServicePane;
    public HBox boxUpdateDelete;
    public ListView<String> listViewServiceSearch;

    private final ServiceDAO serviceDAO = new ServiceDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChoiceBoxes();
        initializeEventHandlers();
        initializeColumns();
        loadInitialServices();
    }

    private void initializeChoiceBoxes() {
        chooseServiceType.setValue("All Services");
        chooseServiceType.getItems().addAll("Massage", "Physio", "Beauty", "Treatment", "Spa", "All Services");
    }

    private void initializeEventHandlers() {
        add.setOnAction(event -> showAddButtonDialog());
        addServiceBtn.setOnAction(event -> handleAddService());
        cancelBtn.setOnAction(event -> resetAddServicePane());
        chooseServiceType.setOnAction(event -> loadServicesByType(chooseServiceType.getValue()));
        searchServiceBtn.setOnAction(event -> searchService());
        listViewServiceSearch.setOnMouseClicked(event -> selectSearchedService());
        editBtn.setOnAction(event -> editService());
        updateServiceBtn.setOnAction(event -> updateService());
        deleteServiceBtn.setOnAction(event -> deleteService());
    }

    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        staffTypeColumn.setCellValueFactory(new PropertyValueFactory<>("staffType"));
    }

    private void loadInitialServices() {
        try {
            loadAllServices();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadServicesByType(String serviceType) {
        try {
            switch (serviceType) {
                case "Massage" -> loadServices("Massage");
                case "Physio" -> loadServices("Physio");
                case "Beauty" -> loadServices("Beauty");
                case "Treatment" -> loadServices("Treatment");
                case "Spa" -> loadServices("Spa");
                default -> loadAllServices();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadServices(String serviceType) throws SQLException {
        List<Service> services = serviceDAO.getServiceByType(serviceType);
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void loadAllServices() throws SQLException {
        List<Service> services = serviceDAO.getAll();
        ObservableList<Service> serviceList = FXCollections.observableArrayList(services);
        servicesTable.setItems(serviceList);
    }

    private void showAddButtonDialog() {
        errorLabel.setVisible(false);
        addServicePane.setVisible(true);
        clearServiceForm();
        populateServiceTypeChoiceBox();
        populateStaffTypeChoiceBox();
    }

    private void clearServiceForm() {
        nameField.clear();
        serviceTypeChoiceBox.getItems().clear();
        durationField.clear();
        priceField.clear();
        staffTypeChoiceBox.getItems().clear();
        descriptionField.clear();
        descriptionField.setPromptText("Description");
    }

    private void populateServiceTypeChoiceBox() {
        serviceTypeChoiceBox.setValue("Choose service type");
        serviceTypeChoiceBox.getItems().addAll("Massage", "Physio", "Beauty", "Treatment", "Spa");
    }

    private void populateStaffTypeChoiceBox() {
        staffTypeChoiceBox.setValue("Choose role");
        staffTypeChoiceBox.getItems().addAll("Beautician", "Physiotherapist", "No Staff Type");
    }

    private void handleAddService() {
        try {
            addService();
            loadAllServices();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addService() throws SQLException {
        String name = nameField.getText();
        String serviceType = serviceTypeChoiceBox.getValue();
        String duration = durationField.getText();
        String cost = priceField.getText();
        String staffType = staffTypeChoiceBox.getValue();
        String description = descriptionField.getText();

        if (validateServiceInput(name, serviceType, duration, cost, staffType)) {
            if (!checkServiceExist(name)) {
                Service service = new Service(null, name, description, Double.parseDouble(cost), serviceType, Integer.parseInt(duration), staffType);
                serviceDAO.insert(service);
                addServicePane.setVisible(false);
            } else {
                showError("Error: Service already exists");
            }
        } else {
            showError("Error: Please fill all the fields");
        }
    }

    private boolean validateServiceInput(String name, String serviceType, String duration, String cost, String staffType) {
        return !name.isEmpty() && !serviceType.isEmpty() && !duration.isEmpty() && !cost.isEmpty() && !staffType.isEmpty();
    }

    private boolean checkServiceExist(String name) throws SQLException {
        List<Service> services = serviceDAO.getAll();
        return services.stream().anyMatch(service -> service.getName().equals(name));
    }

    private void showError(String message) {
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }

    private void resetAddServicePane() {
        addServicePane.setVisible(false);
        boxUpdateDelete.setVisible(false);
        addServiceBtn.setVisible(true);
        errorLabelSearch.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void searchService() {
        String serviceToSearch = searchServiceField.getText();
        try {
            serviceDAO.search(listViewServiceSearch, serviceToSearch);
            listViewServiceSearch.setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectSearchedService() {
        String serviceSelected = listViewServiceSearch.getSelectionModel().getSelectedItem();
        searchServiceField.setText(serviceSelected);
        listViewServiceSearch.setVisible(false);
        errorLabelSearch.setVisible(false);
    }

    private void editService() {
        String serviceName = searchServiceField.getText();
        searchServiceField.clear();
        if (serviceName.isEmpty()) {
            showErrorSearch();
        } else {
            hideErrorSearch();
            populateServiceFormForEdit(serviceName);
        }
    }

    private void showErrorSearch() {
        errorLabelSearch.setVisible(true);
        errorLabelSearch.setText("Error: enter a service name");
    }

    private void hideErrorSearch() {
        errorLabelSearch.setVisible(false);
    }

    private void populateServiceFormForEdit(String serviceName) {
        boxUpdateDelete.setVisible(true);
        addServiceBtn.setVisible(false);
        addServicePane.setVisible(true);
        try {
            Service service = serviceDAO.getServiceByName(serviceName);
            nameField.setText(service.getName());
            descriptionField.setText(service.getDescription());
            durationField.setText(String.valueOf(service.getDuration()));
            priceField.setText(String.valueOf(service.getPrice()));
            serviceTypeChoiceBox.setValue(service.getServiceType());
            staffTypeChoiceBox.setValue(service.getStaffType());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateService() {
        try {
            Service service = getServiceForUpdate();
            if (service != null) {
                serviceDAO.update(service);
                resetAddServicePane();
                loadAllServices();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Service getServiceForUpdate() throws SQLException {
        String oldName = searchServiceField.getText();
        Service service = serviceDAO.getServiceByName(oldName);
        if (service != null && validateServiceInput(nameField.getText(), serviceTypeChoiceBox.getValue(), durationField.getText(), priceField.getText(), staffTypeChoiceBox.getValue())) {
            service.setName(nameField.getText());
            service.setDescription(descriptionField.getText());
            service.setDuration(Integer.parseInt(durationField.getText()));
            service.setPrice(Double.parseDouble(priceField.getText()));
            service.setServiceType(serviceTypeChoiceBox.getValue());
            service.setStaffType(staffTypeChoiceBox.getValue());
            return service;
        } else {
            showError("Error: enter all fields");
            return null;
        }
    }

    private void deleteService() {
        try {
            String oldName = searchServiceField.getText();
            Service service = serviceDAO.getServiceByName(oldName);
            if (service != null) {
                serviceDAO.delete(service.getId());
                loadAllServices();
                resetAddServicePane();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
