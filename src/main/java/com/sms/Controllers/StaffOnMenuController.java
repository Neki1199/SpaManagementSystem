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
import javafx.scene.layout.HBox;

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

    public Button add, updateEmployeeBtn, deleteEmployeeBtn, searchEmployeeBtn, editBtn, addEmployeeBtn, cancelBtn;
    public GridPane dialogAdd;
    public Label errorLabel, errorLblSearch;
    public TextField nameField, usernameField, phoneField, searchEmployeeField;
    public ChoiceBox<String> role;
    public TextField passwordField;
    public CheckBox isadmin;
    public HBox boxUpdateDelete;
    public ListView<String> lisViewEmployeeSearch;

    EmployeeDAO employeeDAO = new EmpDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add.setOnAction(event -> showAddButtonDialog());
        addEmployeeBtn.setOnAction(event -> getEmployeeData());
        cancelBtn.setOnAction(event -> {dialogAdd.setVisible(false); searchEmployeeField.clear();});
        searchEmployeeBtn.setOnAction(event -> searchEmployee());
        lisViewEmployeeSearch.setOnMouseClicked(event -> selectEmployeeList());
        editBtn.setOnAction(event -> editEmployee());
        updateEmployeeBtn.setOnAction(event -> updateEmployee());
        deleteEmployeeBtn.setOnAction(event -> deleteEmployee());

        initializeColumns();
    }

    private void clearAll() {
        nameField.clear();
        usernameField.clear();
        phoneField.clear();
        passwordField.clear();
        errorLblSearch.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void deleteEmployee() {
        try {
            Employee employee = employeeDAO.getEmployeeByName(searchEmployeeField.getText());
            employeeDAO.delete(employee.getId());
            dialogAdd.setVisible(false);
            searchEmployeeField.clear();
            initializeColumns();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEmployeeData(Employee employee) {
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

        try {
            if (validateFields(nameField, phoneField, usernameField, passwordField) && isPhoneValid) {
                employee.setName(name);
                employee.setPhone(phone);
                employee.setAdmin(admin ? 1 : 0);
                employee.setUsername(username);
                employee.setPassword(password);
                employee.setRole(roleEmployee);

                employeeDAO.update(employee);

                dialogAdd.setVisible(false);
            } else if (checkEmployeeExists(name, phone, username)) {
                errorLabel.setVisible(true);
                errorLabel.setText("Employee already exists");
            } else {
                showError(errorLblSearch, "No changes made");
            }
            loadAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEmployee() {
        try {
            Employee employee = employeeDAO.getEmployeeByName(searchEmployeeField.getText());
            if (employee != null) {
                updateEmployeeData(employee);
                initializeColumns();
                dialogAdd.setVisible(false);
                clearAll();
            } else {
                showError(errorLabel, "No changes made");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void editEmployee() {
        try {
            String employeeName = searchEmployeeField.getText();
            Employee employee = employeeDAO.getEmployeeByName(employeeName);
            if (employee == null) {
                showError(errorLblSearch, "Product not found");
            } else {
                nameField.setText(employee.getName());
                usernameField.setText(employee.getUsername());
                phoneField.setText(employee.getPhone());
                passwordField.setText(employee.getPassword());
                role.setValue(employee.getRole());
                if (employee.getAdmin() == 0) {
                    isadmin.setSelected(false);
                } else {
                    isadmin.setSelected(true);
                }
                boxUpdateDelete.setVisible(true);
                addEmployeeBtn.setVisible(false);
                dialogAdd.setVisible(true);
                errorLblSearch.setVisible(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectEmployeeList() {
        String selectedItem = lisViewEmployeeSearch.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] employeeInfo = selectedItem.split("-");
            searchEmployeeField.setText(employeeInfo[0]);
            lisViewEmployeeSearch.setVisible(false);
            errorLblSearch.setVisible(false);
        }
    }

    private void searchEmployee() {
        try {
            String employeeName = searchEmployeeField.getText();
            employeeDAO.search(lisViewEmployeeSearch, employeeName);
            if (lisViewEmployeeSearch.getItems().isEmpty()) {
                showError(errorLblSearch, "Employee not found");
                lisViewEmployeeSearch.setVisible(false);
            } else {
                lisViewEmployeeSearch.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAddButtonDialog() {
        errorLabel.setVisible(false);
        errorLblSearch.setVisible(false);
        dialogAdd.setVisible(true);
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        phoneField.clear();
        role.getItems().clear();
        isadmin.setDisable(false);
        boxUpdateDelete.setVisible(false);
        addEmployeeBtn.setVisible(true);
        try {
            populateRoles();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void populateRoles() throws SQLException {
        role.setValue("Choose role");
        role.getItems().addAll("Supervisor", "Physiotherapist", "Beautician");
    }

    // After click add service button
    public void getEmployeeData() {
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

        try {
            if (validateFields(nameField, phoneField, usernameField, passwordField) && isPhoneValid) {
                Employee employee = new Employee(null, name, roleEmployee, phone, admin ? 1 : 0, username, password);
                employeeDAO.insert(employee);
                dialogAdd.setVisible(false);
            } else if (checkEmployeeExists(name, phone, username)) {
                errorLabel.setVisible(true);
                errorLabel.setText("Employee already exists");
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Please fill all the fields");
            }
            loadAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        try {
            loadAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllEmployees() throws SQLException {
        List<Employee> employees = employeeDAO.getAll();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
        employeeTable.setItems(employeeList);
    }

    private boolean checkEmployeeExists(String n, String pho, String us) throws SQLException {
        boolean employeeExist = false;
        String employeeName = searchEmployeeField.getText();
        Employee employee = employeeDAO.getEmployeeByName(employeeName);

        String name = employee.getName();
        String employeePhone = employee.getPhone();
        String employeeUsername = employee.getUsername();
        if (name.equals(n) || employeePhone.equals(pho) || employeeUsername.equals(us)) {
            employeeExist = true;
        }
        return !employeeExist;
    }

    private boolean validateFields(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
}