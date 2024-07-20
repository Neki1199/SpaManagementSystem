package com.sms.Controllers;

import com.sms.DAO.*;
import com.sms.Models.Client;
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

public class ClientsController extends Node implements Initializable {
    public TableView<Client> clientsTable;
    public TableColumn<Client, Integer> IDColumn;
    public TableColumn<Client, String> nameColumn;
    public TableColumn<Client, String> phoneColumn;
    public TableColumn<Client, String> notesColumn;
    public TableColumn<Client, String> emailColumn;
    public Button add, delete, addClientBtn, cancelBtn, searchClientBtn, editClientBtn, updateClientBtn, deleteClientBtn;
    public GridPane dialogAdd;
    public Label errorLabel, errorLblSearch;
    public TextField nameField, emailField, phoneField, searchClientField;
    public TextArea notesField;
    public ListView<String> lisViewClientSearch;
    public HBox boxUpdateDelete;

    private final ClientDAO clientDAO = new ClientDAOImplement();
    private final AppointmentDAO appointmentDAO = new AptDAOImplement();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add.setOnAction(event -> showAddButtonDialog());
        addClientBtn.setOnAction(event -> addClient());
        cancelBtn.setOnAction(event -> {dialogAdd.setVisible(false);clearAll();});
        searchClientBtn.setOnAction(event -> searchClient());
        lisViewClientSearch.setOnMouseClicked(event -> selectClientFromList());
        editClientBtn.setOnAction(event -> editClient());
        updateClientBtn.setOnAction(event -> updateClient());
        deleteClientBtn.setOnAction(event -> deleteClient());

        initializeColumns();
        loadAllClients();
    }

    private void deleteClientAppointments() throws SQLException {
        Client client = clientDAO.getClientByName(searchClientField.getText());
        appointmentDAO.deleteFromClientID(client.getId());
    }

    private void showAddButtonDialog() {
        clearFields(nameField, emailField, phoneField);
        notesField.clear();
        errorLabel.setVisible(false);
        dialogAdd.setVisible(true);
        boxUpdateDelete.setVisible(false);
        addClientBtn.setVisible(true);
    }

    private void searchClient() {
        try {
            String clientName = searchClientField.getText();
            clientDAO.search(lisViewClientSearch, clientName);
            if (lisViewClientSearch.getItems().isEmpty()) {
                showError(errorLblSearch, "Client not found");
                lisViewClientSearch.setVisible(false);
            } else {
                lisViewClientSearch.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectClientFromList() {
        String selectedItem = lisViewClientSearch.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] clientName = selectedItem.split("-");
            searchClientField.setText(clientName[0]);
            lisViewClientSearch.setVisible(false);
            errorLblSearch.setVisible(false);
        }
    }

    private void editClient() {
        try {
            String clientName = searchClientField.getText();
            Client client = clientDAO.getClientByName(clientName);
            if (client == null) {
                showError(errorLblSearch, "Client not found");
            } else {
                populateEditFields(client);
                boxUpdateDelete.setVisible(true);
                addClientBtn.setVisible(false);
                dialogAdd.setVisible(true);
                errorLblSearch.setVisible(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateClient() {
        try {
            Client client = clientDAO.getClientByName(searchClientField.getText());
            if (client != null) {
                updateClientData(client);
                initializeColumns();
                loadAllClients();
                dialogAdd.setVisible(false);
                clearAll();
            } else {
                showError(errorLabel, "No changes made");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearAll() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        notesField.clear();
        searchClientField.clear();
        lisViewClientSearch.getItems().clear();
        errorLblSearch.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void addClient() {
        try {
            if (validateFields(nameField, emailField, phoneField)) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String notes = notesField.getText();
                if (!checkClientExists(name, phone, email)) {
                    Client client = new Client(null, name, phone, notes, email);
                    clientDAO.insert(client);
                    dialogAdd.setVisible(false);
                    loadAllClients();
                } else {
                    showError(errorLabel, "Error: Client already exists");
                }
            } else {
                showError(errorLabel, "Error: Please fill all the fields");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadAllClients();
    }

    private void loadAllClients() {
        try {
            List<Client> clients = clientDAO.getAll();
            ObservableList<Client> clientsList = FXCollections.observableArrayList(clients);
            clientsTable.setItems(clientsList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkClientExists(String name, String phone, String email) {
        try {
            return clientDAO.getAll().stream()
                    .anyMatch(client -> client.getName().equals(name) || client.getPhone().equals(phone) || client.getEmail().equals(email));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteClient() {
        try {
            deleteClientAppointments();
            Client clientToDelete = clientDAO.getClientByName(searchClientField.getText());
            clientDAO.delete(clientToDelete.getId());
            dialogAdd.setVisible(false);
            errorLblSearch.setVisible(false);
            searchClientField.clear();
            initializeColumns();
            loadAllClients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateClientData(Client client) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String notes = notesField.getText();

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() &&
                (!client.getName().equals(name) || !client.getEmail().equals(email) ||
                        !client.getPhone().equals(phone) || !client.getNotes().equals(notes))) {
            client.setName(name);
            client.setPhone(phone);
            client.setEmail(email);
            client.setNotes(notes);
            try {
                clientDAO.update(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            showError(errorLblSearch, "No changes made");
        }
    }

    private void populateEditFields(Client client) {
        nameField.setText(client.getName());
        phoneField.setText(client.getPhone());
        emailField.setText(client.getEmail());
        notesField.setText(client.getNotes());
    }

    private boolean validateFields(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }


    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
}
