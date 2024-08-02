package com.sms.Controllers;

import com.sms.DAO.*;
import com.sms.Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.sms.Controllers.NotifyAppointmentsChanges.appointmentCountProperty;

public class PaymentsController implements Initializable {
    public Label todayDateLbl, clientNameLbl, totalCostLbl, deleteInfoLabel;
    public TextField searchProductField;
    public Text noAppointmentText;
    public BorderPane paymentPane, addProductPane, addSpaPane;
    public ListView<String> productsList, spaList;
    public TableView<Appointment> paymentTable;
    public TableColumn<Appointment, Integer> IDColumn;
    public TableColumn<Appointment, String> nameColumn, employeeColumn, serviceColumn;
    public Button addProductBtn, addSpaBtn, cashPayBtn, cardPayBtn, deleteProductBtn, cancelBtn, searchProductBtn;
    public Button addProductFinalBtn, cancelAddProductBtn, addSpaFinalBtn, cancelAddSpaBtn;
    public VBox serviceProductBox, costBox;

    private final AppointmentDAO appointmentDAO = new AptDAOImplement();
    private final ClientDAO clientDAO = new ClientDAOImplement();
    private final EmployeeDAO employeeDAO = new EmpDAOImplement();
    private final ServiceDAO serviceDAO = new ServiceDAOImplement();
    private final InventoryDAO inventoryDAO = new InvDAOImplement();

    private List<Appointment> notPaidAppointments;

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
            if (newCount != null && newCount.intValue() != oldCount.intValue()) {
                try {
                    initializeColumns(today);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        paymentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paymentPane.setVisible(true);
                try {
                    setPaymentLabels();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancelBtn.setOnAction(event -> {
            paymentPane.setVisible(false);
            clearPaymentPane();
            paymentTable.getSelectionModel().clearSelection();
        });

        addProductBtn.setOnAction(event -> addProductPane.setVisible(true));
        addSpaBtn.setOnAction(event -> showSpaServices());
        cancelAddProductBtn.setOnAction(event -> {
            addProductPane.setVisible(false);
            productsList.getItems().clear();
            searchProductField.clear();
        });
        cancelAddSpaBtn.setOnAction(event -> {addSpaPane.setVisible(false); spaList.getItems().clear();});
        searchProductBtn.setOnAction(event -> searchProduct());
        addProductFinalBtn.setOnAction(event -> addProduct());
        addSpaFinalBtn.setOnAction(event -> addSpa());
        deleteProductBtn.setOnAction(event -> deleteServiceProduct());
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
        List<Appointment> appointments = appointmentDAO.getFromDate(String.valueOf(today));
        paymentTable.getItems().clear();
        noAppointmentText.setVisible(false);

        // get appointment with status not paid
        if (appointments.isEmpty()) {
            noAppointmentText.setVisible(true);
        } else {
            notPaidAppointments = appointmentDAO.getNotPaid(appointments);
            if (notPaidAppointments.isEmpty()) {
                paymentTable.getItems().clear();
                noAppointmentText.setVisible(true);
            } else {
                ObservableList<Appointment> notPaidAppointmentsList = FXCollections.observableArrayList(notPaidAppointments);
                paymentTable.setItems(notPaidAppointmentsList);
            }
        }
    }

    // check if the client has more appointments the same day
    private List<Appointment> checkClientAppointments(Integer clientID, List<Appointment> appointmentsToPay) throws SQLException {
        List<Appointment> allAppointments = new ArrayList<>();
        for (Appointment appointment : appointmentsToPay) {
            if (clientID.equals(appointment.getClientId())) {
                allAppointments.add(appointment);
            }
        }
        return allAppointments;
    }

    private void addLabelToBox(VBox box, String text, String styleWidth){
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-background-color: #ffffff; -fx-background-radius: 5;" +
                "-fx-font-size: 15; -fx-pref-width: "+ styleWidth +"; -fx-pref-height: 50; -fx-alignment: center;");
        box.getChildren().add(label);
    }

    private void updateTotalCost(Double cost){
        double totalCost = Double.parseDouble(totalCostLbl.getText());
        totalCost += cost;
        totalCostLbl.setText(String.valueOf(totalCost));
    }

    private void createServiceLabel(Appointment appointment) throws SQLException {
        clientNameLbl.setText(clientDAO.get(appointment.getClientId()).getName());
        Service service = serviceDAO.get(appointment.getServiceId());

        addLabelToBox(serviceProductBox, service.getName(), "300");
        addLabelToBox(costBox, String.valueOf(service.getPrice()), "100");
        updateTotalCost(service.getPrice());
    }

    private void setPaymentLabels() throws SQLException {
        Appointment appointment = paymentTable.getSelectionModel().getSelectedItem();
        List<Appointment> clientAppointments = checkClientAppointments(appointment.getClientId(), notPaidAppointments);
        for (Appointment clientAppointment : clientAppointments) {
            createServiceLabel(clientAppointment);
        }
    }

    private void searchProduct(){
        String productToSearch = searchProductField.getText();
        try {
            inventoryDAO.search(productsList, productToSearch);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addProduct() {
        String productToAdd = productsList.getSelectionModel().getSelectedItem();
        try {
            Inventory product = inventoryDAO.getProductByName(productToAdd);
            addLabelToBox(serviceProductBox, product.getName(), "300");
            addLabelToBox(costBox, String.valueOf(product.getCost()), "100");
            updateTotalCost(product.getCost());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addProductPane.setVisible(false);
        productsList.getItems().clear();
        searchProductField.clear();
    }

    private void addSpa(){
        String spaToAdd = spaList.getSelectionModel().getSelectedItem();
        try {
            Service spaService = serviceDAO.getServiceByName(spaToAdd);
            addLabelToBox(serviceProductBox, spaService.getName(), "300");
            addLabelToBox(costBox, String.valueOf(spaService.getPrice()), "100");
            updateTotalCost(spaService.getPrice());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addSpaPane.setVisible(false);
    }

    private void clearPaymentPane(){
        serviceProductBox.getChildren().clear();
        costBox.getChildren().clear();
        totalCostLbl.setText("0");
    }

    private void showSpaServices(){
        addSpaPane.setVisible(true);
        try {
            spaList.getItems().clear();
            List<Service> spaServices = serviceDAO.getServiceByType("Spa");
            for(Service service : spaServices) {
                spaList.getItems().add(service.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteServiceProduct(){
        boolean[] deleted = {false}; // flag to not let delete more than one label
        deleteInfoLabel.setVisible(true);
        for(Node node : serviceProductBox.getChildren()){
            if(node instanceof Label label){
                label.setOnMouseClicked(event -> {
                    if(!deleted[0]) {
                        int indexLabel = serviceProductBox.getChildren().indexOf(label);
                        double cost = Double.parseDouble(totalCostLbl.getText());

                        Label costLabel = (Label) costBox.getChildren().get(indexLabel);
                        cost -= Double.parseDouble(costLabel.getText());
                        totalCostLbl.setText(String.valueOf(cost));

                        serviceProductBox.getChildren().remove(label);
                        costBox.getChildren().remove(indexLabel);
                        deleteInfoLabel.setVisible(false);

                        deleted[0] = true;
                    }
                });
            }
        }
    }
}