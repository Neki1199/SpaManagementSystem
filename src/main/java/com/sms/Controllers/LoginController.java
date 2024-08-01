package com.sms.Controllers;

import com.sms.DAO.EmpDAOImplement;
import com.sms.DAO.EmployeeDAO;
import com.sms.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField userInput;
    public PasswordField passwordInput;
    public Button loginButton;
    public Label loginErrorLabel;

    EmployeeDAO employeeDAO = new EmpDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // while testing, then delete
        loginButton.setOnAction(event -> {
            onLogin();
        });
        // Creates an instance of ViewFactory to show view of Staff (Staff Menu)
//        loginErrorLabel.setVisible(false);
//
//        List<Employee> employeeList;
//        try {
//            employeeList = employeeDAO.getAll();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        loginButton.setOnAction(event -> {
//            // get username and password
//            String username = userInput.getText();
//            String password = passwordInput.getText();
//            Employee employee = null;
//
//            for (Employee employeeTemp : employeeList) {
//                if (employeeTemp.getUsername().equals(username) && employeeTemp.getPassword().equals(password)) {
//                    employee = employeeTemp;
//                } else if(employeeTemp.getUsername().equals(username) && !employeeTemp.getPassword().equals(password)) {
//                    employee = employeeTemp;
//                }
//            }
//
//            if(employee != null && employee.getUsername().equals(username) && employee.getPassword().equals(password)){
//                if(employee.getAdmin() == 1) {
//                    Model.getInstance().getViewFactory().showAdminAppointments(); // Enter admin
//                }else{
//                    Model.getInstance().getViewFactory().showStaffAppointments(); // Enter staff
//                }
//            } else{
//                if(!username.isEmpty() && !password.isEmpty()){
//                    loginErrorLabel.setText("Error: incorrect details");
//                    loginErrorLabel.setVisible(true);
//                } else{
//                    loginErrorLabel.setText("Error: enter details");
//                    loginErrorLabel.setVisible(true);
//                }
//            }
//        onLogin();
//        });
    }
    private void onLogin(){
        // to retrieve the stage of that object (if not, is not possible to get the stage)
        Stage stage = (Stage) loginErrorLabel.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showAdmin();
    }
}
