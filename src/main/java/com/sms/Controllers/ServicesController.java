package com.sms.Controllers;

import com.sms.DAO.ServiceDAO;
import com.sms.DAO.ServiceDAOImplement;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ServicesController extends Node implements Initializable {
    public Button add;
    public Pane dialogAdd;
    public Button addServiceBtn;
    public Button cancelBtn;
    public Label errorLabel;
    public TextField nameField;
    public ChoiceBox serviceTypeChoiceBox;
    public TextField durationField;
    public TextField costField;
    public ChoiceBox staffTypeChoiceBox;
    public TextArea descriptionField;
    public ChoiceBox chooseServiceType;

    ServiceDAO serviceDAO = new ServiceDAOImplement();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}