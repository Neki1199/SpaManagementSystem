package com.sms.Controllers;

import com.sms.Models.Employee;
import com.sms.DAO.EmpDAOImplement;
import com.sms.DAO.EmployeeDAO;
import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateFirstUserController implements Initializable {
    public TextField fullname, phoneno, username, password;
    public ChoiceBox<String> role;
    public CheckBox isadmin;
    public Button createAccount;
    public Label errorlbl;

    final EmployeeDAO employeeDAO = new EmpDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateChoiceBoxRole();

        createAccount.setOnAction(event ->{
            try {
                Employee employee = getEmployeeData();
                if(employee != null) {
                    onCreateUser();
                }
            } catch (SQLException e) {
                errorlbl.setVisible(true);
                errorlbl.setText("Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    private Employee getEmployeeData() throws SQLException {
        Employee employee = null;
        String name = fullname.getText();
        String phone = phoneno.getText();
        String user = username.getText();
        String pass = password.getText();
        String roleEmployee = role.getValue();
        boolean admin = isadmin.isSelected();

        // validate phone with regex
        if (!phone.matches("^\\d{10,15}$")) {
            errorlbl.setVisible(true);
            errorlbl.setText("Error: invalid phone number");
            return null;
        }

        // create employee
        if(!name.isEmpty() && !user.isEmpty() && !pass.isEmpty() && !roleEmployee.equals("Select Employee Role")){
            employee = new Employee(null, name, roleEmployee, phone, admin ? 1 : 0, user, pass);
            employeeDAO.insert(employee);
        }else{
            errorlbl.setVisible(true);
            errorlbl.setText("Error: enter all details");
        }
        return employee;
    }

    private void onCreateUser(){
        // to retrieve the stage of that object (if not, is not possible to get the stage)
        Stage stage = (Stage) errorlbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLogin();
    }

    private void populateChoiceBoxRole(){
        role.setValue("Select Employee Role");
        role.getItems().add("Supervisor");
        role.getItems().add("Physiotherapist");
        role.getItems().add("Beautician");
    }
}