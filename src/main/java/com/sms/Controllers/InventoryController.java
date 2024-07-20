package com.sms.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController extends Node implements Initializable {
    public TableView servicesTable;
    public TableColumn IDColumn;
    public TableColumn nameColumn;
    public TableColumn descriptionColumn;
    public TableColumn quantityColumn;
    public TableColumn priceColumn;
    public GridPane dialogAdd;
    public Label errorLabel;
    public TextField nameField;
    public TextField priceField;
    public TextField quantityField;
    public TextField costField;
    public TextArea descriptionField;
    public Button updateProductBtn;
    public Button deleteProductBtn;
    public Button addProductBtn;
    public Button cancelBtn;
    public Button add;
    public TextField searchProductField;
    public Button searchProductBtn;
    public Button editBtn;
    public ListView listViewProductSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}