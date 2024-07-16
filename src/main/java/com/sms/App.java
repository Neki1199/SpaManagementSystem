package com.sms;

import com.sms.DAO.EmpDAOImplement;
import com.sms.DAO.EmployeeDAO;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws SQLException {
        EmployeeDAO employeeDAO = new EmpDAOImplement();
        boolean userExist = employeeDAO.usersExist();
        // Shows login View if a user exists, else, create a user view
        if(userExist){
            Model.getInstance().getViewFactory().showLogin();
        } else{
            Model.getInstance().getViewFactory().showCreateUser();
        }
    }
}
