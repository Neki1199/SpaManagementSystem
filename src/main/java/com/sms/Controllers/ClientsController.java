package com.sms.Controllers;

import com.sms.ConnectDB;
import com.sms.DAO.AppointmentDAO;
import com.sms.DAO.AptDAOImplement;
import com.sms.DAO.ClientDAO;
import com.sms.DAO.ClientDAOImplement;
import com.sms.Models.Appointment;
import com.sms.Models.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController extends Node implements Initializable {
    public TableView<Client> clientsTable;
    public TableColumn<Client, Integer> IDColumn;
    public TableColumn<Client, String> nameColumn;
    public TableColumn<Client, String> phoneColumn;
    public TableColumn<Client, String> notesColumn;
    public TableColumn<Client, String> emailColumn;
    public Button add;
    public Button delete;
    public GridPane dialogAdd;
    public Button addClientBtn;
    public Button cancelBtn;
    public Label errorLabel;
    public TextField nameField;
    public TextArea notesField;
    public TextField emailField;
    public TextField phoneField;
    public TextField searchClientField;
    public Button searchClientBtn;
    public ListView<String> lisViewClientSearch;
    public Label errorLblSearch;
    public Button editClientBtn;
    public TextField editName;
    public TextArea editNotes;
    public TextField editEmail;
    public TextField editPhone;
    public Button cancelEditBtn;
    public Button updateClientBtn;
    public Button deleteClientBtn;
    public GridPane editClientPane;

    ClientDAO clientDAO = new ClientDAOImplement();
    AppointmentDAO appointmentDAO = new AptDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        add.setOnAction(event -> {
            try {
                showAddButtonDialog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        addClientBtn.setOnAction(event -> {
            try {
                getClientData();
                loadAllClients();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        cancelBtn.setOnAction(event -> {
            dialogAdd.setVisible(false);
        });

        searchClientBtn.setOnAction(event -> {
            try {
                errorLblSearch.setVisible(false);
                searchClient();
                if (lisViewClientSearch.getItems().isEmpty()) {
                    lisViewClientSearch.setVisible(false);
                    errorLblSearch.setText("Client not found");
                    errorLblSearch.setVisible(true);
                } else {
                    lisViewClientSearch.setVisible(true);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        lisViewClientSearch.setOnMouseClicked(event -> {
            String nameAndPhoneClient = lisViewClientSearch.getSelectionModel().getSelectedItem();
            String[] clientName = nameAndPhoneClient.split("-");
            searchClientField.setText(clientName[0]);
            lisViewClientSearch.setVisible(false);
            errorLblSearch.setVisible(false);
        });

        editClientBtn.setOnAction(event -> {
            try {
                editClient();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        updateClientBtn.setOnAction(event -> {
            updateClient();
        });

        deleteClientBtn.setOnAction(event -> {
            try {
                deleteClientAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            deleteClient();
        });

        cancelEditBtn.setOnAction(event -> {
            clearEdit();
        });

        try {
            initializeColumns();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // load clients initially
        try {
            loadAllClients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteClientAppointments() throws SQLException {
        String clientName = searchClientField.getText();
        Client client = clientDAO.getClientByName(clientName);
        appointmentDAO.deleteFromClientID(client.getId());
    }

    // Event when add Button is clicked (set visible the dialog, and all services to choicebox)
    public void showAddButtonDialog() throws SQLException {
        errorLabel.setVisible(false);
        dialogAdd.setVisible(true);
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        notesField.clear();
    }

    // search function for searchClientField
    private void searchClient() throws SQLException {
        String clientName = searchClientField.getText();
        clientDAO.search(lisViewClientSearch, clientName);
    }

    private void editClient() throws SQLException {
        String clientName = searchClientField.getText();
        if(clientName.isEmpty()){
            errorLblSearch.setText("Enter Client Full Name");
            errorLblSearch.setVisible(true);
        } else if(clientDAO.getClientByName(clientName) == null){
            errorLblSearch.setText("Client not found");
            errorLblSearch.setVisible(true);
        } else {
            Client client = clientDAO.getClientByName(clientName);

            editClientPane.setVisible(true);
            errorLblSearch.setVisible(false);

            editName.setText(client.getName());
            editPhone.setText(client.getPhone());
            editEmail.setText(client.getEmail());
            editNotes.setText(client.getNotes());
        }
    }

    private void updateClient(){
        Client client = null;
        try {
            client = clientDAO.getClientByName(searchClientField.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String name = editName.getText();
        String email = editEmail.getText();
        String phone = editPhone.getText();
        String notes = editNotes.getText();

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && (!client.getName().equals(name) || !client.getEmail().equals(email) || !client.getPhone().equals(phone) || !client.getNotes().equals(notes))) {
            client.setName(name);
            client.setPhone(phone);
            client.setEmail(email);
            client.setNotes(notes);
            try {
                updateClient(client);
                initializeColumns();
                loadAllClients();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clearEdit();
        } else {
            errorLblSearch.setVisible(true);
            errorLblSearch.setText("No changes made");
        }
    }

    // After click add service button
    public void getClientData() throws SQLException {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String notes = notesField.getText();

        boolean exists = checkClientExists(name, phone, email);
        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !exists) {
            Client client = new Client(null, name, phone, notes, email);
            clientDAO.insert(client);
            dialogAdd.setVisible(false);
        } else if (exists) {
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Client already exists");
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Please fill all the fields");
        }
    }

    private void initializeColumns() throws SQLException {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        // to edit tables
        clientsTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setName(event.getNewValue());
            updateClient(client);
        });

        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setPhone(event.getNewValue());
            updateClient(client);
        });

        notesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        notesColumn.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setNotes(event.getNewValue());
            updateClient(client);
        });

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setEmail(event.getNewValue());
            updateClient(client);
        });
        loadAllClients();
    }

    private void updateClient(Client client) {
        try {
            clientDAO.update(client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteClient() {
        String clientName = editName.getText();
        try {
            Client clientToDelete = clientDAO.getClientByName(clientName);
            clientDAO.delete(clientToDelete.getId());
            editClientPane.setVisible(false);
            errorLblSearch.setVisible(false);
            searchClientField.clear();
            initializeColumns();
            loadAllClients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllClients() throws SQLException {
        List<Client> clients = clientDAO.getAll();
        ObservableList<Client> clientsList = FXCollections.observableArrayList(clients);
        clientsTable.setItems(clientsList);
    }

    public boolean checkClientExists(String n, String pho, String em) throws SQLException {
        boolean clientExist = false;
        List<Client> clients = clientDAO.getAll();
        for (Client client : clients) {
            String clientName = client.getName();
            String clientPhone = client.getPhone();
            String clientEmail = client.getEmail();
            if (clientName.equals(n) || clientPhone.equals(pho) || clientEmail.equals(em)) {
                clientExist = true;
                break;
            }
        }
        return clientExist;
    }

    private void clearEdit() {
        errorLblSearch.setVisible(false);
        editClientPane.setVisible(false);
        searchClientField.clear();
        editName.clear();
        editPhone.clear();
        editEmail.clear();
        editNotes.clear();
    }
}