package com.sms.Controllers;

import com.sms.DAO.ClientDAO;
import com.sms.DAO.ClientDAOImplement;
import com.sms.Models.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

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
    public Button add;
    public Button edit;
    public Button delete;
    public GridPane dialogAdd;
    public Button addClientBtn;
    public Button cancelBtn;
    public Label errorLabel;
    public TextField nameField;
    public TextArea notesField;
    public TextField emailField;
    public TextField phoneField;

    ClientDAO clientDAO = new ClientDAOImplement();
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

        initializeColumns();

        // load clients initally
        try{
            loadAllClients();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
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

    // After click add service button
    public void getClientData() throws SQLException {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String notes = notesField.getText();

        boolean exists = checkClientExists(name, phone, email);
        if(!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !exists) {
            Client client = new Client(null, name, phone, notes, email);
            clientDAO.insert(client);
            dialogAdd.setVisible(false);
        }else if(exists){
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Client already exists");
        }else{
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Please fill all the fields");
        }
    }

    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllClients() throws SQLException {
        List<Client> clients = clientDAO.getAll();
        ObservableList<Client> clientsList = FXCollections.observableArrayList(clients);
        clientsTable.setItems(clientsList);
    }

    private boolean checkClientExists(String n, String pho, String em) throws SQLException {
        boolean clientExist = false;
        List<Client> clients = clientDAO.getAll();
        for(Client client : clients) {
            String clientName = client.getName();
            String clientPhone = client.getPhone();
            String clientEmail = client.getEmail();
            if(clientName.equals(n) || clientPhone.equals(pho) || clientEmail.equals(em)){
                clientExist = true;
                break;
            }
        }
        return clientExist;
    }
}