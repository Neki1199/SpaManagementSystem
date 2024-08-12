package com.sms.Controllers;

import com.sms.DAO.*;
import com.sms.Models.Client;
import com.sms.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
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

import static com.sms.Controllers.NotifyAppointmentsChanges.appointmentCountProperty;

public class InvoicesController extends Node implements Initializable {
    public TableView<Invoice> invoicesTable;
    public TableColumn<Invoice, Integer> invoiceIDColumn, clientIDColumn;
    public TableColumn<Invoice, String> clientNameColumn, dateColumn, paymentMethodColumn;
    public TableColumn<Invoice, Double> totalCostColumn;
    public GridPane detailsPane;
    public Label errorLabel, errorLabelSearch;
    public TextField invoiceIDField, clientIDField, dateField, costField, nameField, paymentMethodField, searchClientField;
    public Button cancelBtn, searchClientBtn, allInvoicesBtn;
    public ListView<String> itemsList, clientListSearch;

    InvoiceDAO invoiceDAO = new InvoiceDAOImplement();
    ClientDAO clientDAO = new ClientDAOImplement();
    InvoiceItemDAO itemDAO = new InvItemDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeColumns();
        allInvoices();

        // when the NotifyChangesAppointments get a change, update this ui
        appointmentCountProperty().addListener((obs, oldCount, newCount) -> {
            if (newCount != null && newCount.intValue() != oldCount.intValue()) {
                allInvoices();
            }
        });

        cancelBtn.setOnAction(event -> cancelPane());
        searchClientBtn.setOnAction(event -> searchClient());
        clientListSearch.setOnMouseClicked(event -> searchClientField());
        allInvoicesBtn.setOnAction(event -> allInvoices());
        invoicesTable.setOnMouseClicked(event -> {
            try {
                showInvoiceDetails();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeColumns() {
        invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        clientIDColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameColumn.setCellValueFactory(cellData -> {
            try {
                Client client = clientDAO.get(cellData.getValue().getClientId());
                return new SimpleStringProperty(client.getName());
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown");
            }
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }

    private void cancelPane() {
        invoicesTable.getSelectionModel().clearSelection();
        detailsPane.setVisible(false);
        invoiceIDField.clear();
        clientIDField.clear();
        dateField.clear();
        paymentMethodField.clear();
        dateField.clear();
        errorLabel.setVisible(false);
    }

    private void searchClient() {
        String clientName = searchClientField.getText();
        try {
            clientDAO.search(clientListSearch, clientName);
            if(clientListSearch.getItems().isEmpty()){
                errorLabelSearch.setVisible(true);
                errorLabelSearch.setText("Client Not Found");
            }else{
                clientListSearch.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void searchClientField() {
        String selectedClient = clientListSearch.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                String[] clientName = selectedClient.split("-");
                Client client = clientDAO.getClientByName(clientName[0]);
                loadInvoicesClient(client);
                searchClientField.setText(clientName[0]);
                clientListSearch.setVisible(false);
                errorLabelSearch.setVisible(false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadInvoicesClient(Client client){
        try {
            List<Invoice> clientInvoices = invoiceDAO.getByClient(client.getId());
            ObservableList<Invoice> invoices = FXCollections.observableArrayList(clientInvoices);
            invoicesTable.setItems(invoices);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void allInvoices() {
        searchClientField.clear();
        List<Invoice> allInvoices = null;
        try {
            allInvoices = invoiceDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Invoice> invoices = FXCollections.observableArrayList(allInvoices);
        invoicesTable.setItems(invoices);
    }

    private void showInvoiceDetails() throws SQLException {
        Invoice invoice = invoicesTable.getSelectionModel().getSelectedItem();
        if(invoice != null){
            Client client = clientDAO.get(invoice.getClientId());
            detailsPane.setVisible(true);
            invoiceIDField.setText(String.valueOf(invoice.getInvoiceId()));
            clientIDField.setText(String.valueOf(invoice.getClientId()));
            nameField.setText(client.getName());
            dateField.setText(invoice.getOrderDate());
            paymentMethodField.setText(invoice.getPaymentMethod());
            costField.setText(String.valueOf(invoice.getTotalCost()));

            itemDAO.search(itemsList, invoice.getInvoiceId());
        }
    }
}