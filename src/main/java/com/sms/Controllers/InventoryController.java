package com.sms.Controllers;

import com.sms.DAO.InvDAOImplement;
import com.sms.DAO.InventoryDAO;
import com.sms.Models.Client;
import com.sms.Models.Inventory;
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

public class InventoryController extends Node implements Initializable {
    public TableView<Inventory> inventoryTable;
    public TableColumn<Inventory, Integer> IDColumn, quantityColumn;
    public TableColumn<Inventory, String> nameColumn, descriptionColumn;
    public TableColumn<Inventory, Double> priceColumn;
    public GridPane dialogAdd;
    public Label errorLabel;
    public TextField nameField, priceField, quantityField, searchProductField;
    public TextArea descriptionField;
    public Button updateProductBtn, deleteProductBtn, addProductBtn, cancelBtn, add, searchProductBtn, editBtn;
    public ListView<String> listViewProductSearch;
    public HBox boxUpdateDelete;
    public Label errorLabelSearch;
    public TextField urlField;

    InventoryDAO inventoryDAO = new InvDAOImplement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeColumns();
        add.setOnAction(event -> addBtn());
        cancelBtn.setOnAction(event -> cancelAdd());
        addProductBtn.setOnAction(event -> addProduct());
        searchProductBtn.setOnAction(event -> searchProduct());
        listViewProductSearch.setOnMouseClicked(event -> selectProductFromList());
        editBtn.setOnAction(event -> editProduct());
        updateProductBtn.setOnAction(event -> updateProduct());
        deleteProductBtn.setOnAction(event -> deleteProduct());
    }

    private void deleteProduct() {
        try {
            Inventory product = inventoryDAO.getProductByName(searchProductField.getText());
            inventoryDAO.delete(product.getId());
            dialogAdd.setVisible(false);
            searchProductField.clear();
            initializeColumns();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void searchProduct() {
        try {
            String productName = searchProductField.getText();
            inventoryDAO.search(listViewProductSearch, productName);
            if (listViewProductSearch.getItems().isEmpty()) {
                showError(errorLabelSearch, "Product not found");
                listViewProductSearch.setVisible(false);
            } else {
                listViewProductSearch.setVisible(true);
            }
        }catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }
    private void selectProductFromList() {
        String selectedItem = listViewProductSearch.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            searchProductField.setText(selectedItem);
            listViewProductSearch.setVisible(false);
            errorLabelSearch.setVisible(false);
        }
    }

    private void editProduct() {
        try {
            String productName = searchProductField.getText();
            Inventory product = inventoryDAO.getProductByName(productName);
            if (product == null) {
                showError(errorLabelSearch, "Product not found");
            } else {
                nameField.setText(product.getName());
                descriptionField.setText(product.getDescription());
                quantityField.setText(String.valueOf(product.getQuantity()));
                priceField.setText(String.valueOf(product.getCost()));
                urlField.setText(product.getUrl());
                boxUpdateDelete.setVisible(true);
                addProductBtn.setVisible(false);
                dialogAdd.setVisible(true);
                errorLabelSearch.setVisible(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateProduct() {
        try {
            Inventory inventory = inventoryDAO.getProductByName(searchProductField.getText());
            if (inventory != null) {
                updateProductData(inventory);
                initializeColumns();
                loadAllProducts();
                dialogAdd.setVisible(false);
                clearAll();
            } else {
                showError(errorLabel, "No changes made");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateProductData(Inventory inventory) {
        String name = nameField.getText();
        String description = descriptionField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double cost = Double.parseDouble(priceField.getText());
        String url = urlField.getText();

        if (!name.isEmpty() || quantity >= 0 || cost >= 0 || !inventory.getDescription().equals(description) ||
                !inventory.getName().equals(name) || inventory.getQuantity() != quantity ||
                inventory.getCost() != cost || !inventory.getUrl().equals(url))
        {
            inventory.setName(name);
            inventory.setQuantity(quantity);
            inventory.setCost(cost);
            inventory.setDescription(description);
            inventory.setUrl(url);

            try {
                inventoryDAO.update(inventory);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            showError(errorLabelSearch, "No changes made");
        }
    }

    private void clearAll() {
        nameField.clear();
        descriptionField.clear();
        quantityField.clear();
        priceField.clear();
        searchProductField.clear();
        urlField.clear();
        listViewProductSearch.getItems().clear();
        errorLabelSearch.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void addBtn(){
        dialogAdd.setVisible(true);
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        descriptionField.clear();
        urlField.clear();
        errorLabel.setVisible(false);
        boxUpdateDelete.setVisible(false);
        addProductBtn.setVisible(true);
    }

    private void addProduct() {
        try {
            if (validateFields(nameField, priceField, quantityField)) {
                String name = nameField.getText();
                String price = priceField.getText();
                String quantity = quantityField.getText();
                String description = descriptionField.getText();
                String url = urlField.getText();

                if (!checkProductExists(name)) {
                    Inventory newProduct = new Inventory(null, name, Integer.parseInt(quantity), Double.parseDouble(price), description, url);
                    inventoryDAO.insert(newProduct);
                    dialogAdd.setVisible(false);
                    loadAllProducts();
                } else {
                    showError(errorLabel, "Product already exists");
                }
            } else {
                showError(errorLabel, "Please fill all the fields");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void cancelAdd() {
        dialogAdd.setVisible(false);
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        descriptionField.clear();
        urlField.clear();
        errorLabel.setVisible(false);
    }

    private void initializeColumns() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadAllProducts();
    }

    private void loadAllProducts() {
        try {
            List<Inventory> inventory = inventoryDAO.getAll();
            ObservableList<Inventory> inventoryList = FXCollections.observableArrayList(inventory);
            inventoryTable.setItems(inventoryList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateFields(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkProductExists(String name) {
        try {
            return inventoryDAO.getAll().stream()
                    .anyMatch(client -> client.getName().equals(name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
}