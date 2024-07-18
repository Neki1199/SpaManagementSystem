package com.sms.Controllers;

import com.sms.DAO.EmpDAOImplement;
import com.sms.DAO.EmployeeDAO;
import com.sms.Models.Employee;
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

public class StaffOnMenuController extends Node implements Initializable {
    public TableView<Employee> employeeTable;
    public TableColumn<Employee, Integer> IDColumn;
    public TableColumn<Employee, String> nameColumn;
    public TableColumn<Employee, String> phoneColumn;
    public TableColumn<Employee, Integer> adminColumn;
    public TableColumn<Employee, String> usernameColumn;
    public TableColumn<Employee, String> roleColumn;

    public Button add;
    public Button edit;
    public Button delete;
    public GridPane dialogAdd;
    public Button addEmployeeBtn;
    public Button cancelBtn;
    public Label errorLabel;
    public TextField nameField;
    public TextField usernameField;
    public TextField phoneField;
    public ChoiceBox<String> role;
    public TextField passwordField;
    public CheckBox isadmin;

    EmployeeDAO employeeDAO = new EmpDAOImplement();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add.setOnAction(event -> {
            try {
                showAddButtonDialog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        addEmployeeBtn.setOnAction(event -> {
            try {
                getEmployeeData();
                loadAllEmployees();
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
            loadAllEmployees();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Event when add Button is clicked (set visible the dialog, and all services to choicebox)
    public void showAddButtonDialog() throws SQLException {
        errorLabel.setVisible(false);
        dialogAdd.setVisible(true);
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        phoneField.clear();
        role.getItems().clear();
        isadmin.setDisable(false);
        populateRoles();
    }

    public void populateRoles() throws SQLException {
        role.setValue("Choose role");
        role.getItems().addAll("Supervisor", "Physiotherapist", "Beautician");
    }

    // After click add service button
    public void getEmployeeData() throws SQLException {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String roleEmployee = role.getValue();
        boolean admin = isadmin.isSelected();
        boolean isPhoneValid = true;

        // validate phone with regex
        if (!phone.matches("^\\d{10,15}$")) {
            isPhoneValid = false;
            errorLabel.setVisible(true);
            errorLabel.setText("Error: invalid phone number");
        }

        boolean exists = checkEmployeeExists(name, phone, username);
        if(!name.isEmpty() && !phone.isEmpty() && !username.isEmpty() && !password.isEmpty() && !roleEmployee.isEmpty() && !exists && isPhoneValid) {
            Employee employee = new Employee(null, name, roleEmployee, phone, admin ? 1 : 0, username, password);
            employeeDAO.insert(employee);
            dialogAdd.setVisible(false);
        }else if(exists){
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Employee already exists");
        }else{
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Please fill all the fields");
        }
    }

    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void loadAllEmployees() throws SQLException {
        List<Employee> employees = employeeDAO.getAll();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
        employeeTable.setItems(employeeList);
    }

    private boolean checkEmployeeExists(String n, String pho, String us) throws SQLException {
        boolean employeeExist = false;
        List<Employee> employees = employeeDAO.getAll();
        for(Employee employee : employees) {
            String employeeName = employee.getName();
            String employeePhone = employee.getPhone();
            String employeeUsername = employee.getUsername();
            if(employeeName.equals(n) || employeePhone.equals(pho) || employeeUsername.equals(us)){
                employeeExist = true;
                break;
            }
        }
        return employeeExist;
    }
}