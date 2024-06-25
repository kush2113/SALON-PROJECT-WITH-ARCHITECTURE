package lk.ijse.demokushan.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.demokushan.Util.Regex;
import lk.ijse.demokushan.model.*;
import lk.ijse.demokushan.model.TM.HairCutTM;
import lk.ijse.demokushan.model.TM.PaymentTM;
import lk.ijse.demokushan.model.TM.ProductTM;
import lk.ijse.demokushan.repository.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductFormController {

    public AnchorPane rootNode;

    public Rectangle rectangal;
    public TextField txtPId;
    public TextField txtUPrice;
    public TableColumn colUPrice;
    public TableView TbleProduct;
    public TableColumn colPId;
    public TableColumn colName;
    public TextField txtPName;
    public TextField txtQtyOnHand;
    public TableColumn colQtyOnHand;
    public TableColumn colTotal;
    public TableColumn colAction;
    public ComboBox cmbSupplierId;
    public ComboBox cmbProductName;
    public TextField txtQty;
    public TextField txtSearch;


    public void initialize() {
        setcellValues();
        loadAllProduct();
        genarateNextProductId();
        loadCmbSupplierId();
        loadAllProductNames();
        showSelectedProductDetails();
    }
    private void showSelectedProductDetails() {
        ProductTM selectedUser = (ProductTM) TbleProduct.getSelectionModel().getSelectedItem();
        TbleProduct.setOnMouseClicked(event -> showSelectedProductDetails());
        if (selectedUser != null) {

            txtPId.setText(selectedUser.getProductId());
            txtPName.setText(selectedUser.getName());
            txtUPrice.setText(selectedUser.getUnitPrice());
            txtQtyOnHand.setText(selectedUser.getQtyOnHand());

        }
    }

    private void loadAllProductNames() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = HairCutRepo.getProductNames();

            for (String code : nameList) {
                obList.add(code);
            }

            cmbProductName.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCmbSupplierId() {


        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = SupplierRepo.getSupplierId();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbSupplierId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void genarateNextProductId() {
        try {
            String currentId = ProductRepo.getCurrentId();

            String nextOrderId = genarateNextProductId(currentId);
            txtPId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String genarateNextProductId(String currentId) {
        if (currentId != null && currentId.matches("P\\d{3}")) {
            int idNum = Integer.parseInt(currentId.substring(2));
            return "P" + String.format("%03d", idNum + 1);
        } else {
            return "P001";
        }
    }

    private void loadAllProduct() {

        ObservableList<ProductTM> obList = FXCollections.observableArrayList();

        try {
            List<Product> productsList = ProductRepo.getAll();
            for (Product proModle : productsList) {

                ProductTM TM = new ProductTM(proModle.getProductId(), proModle.getName(), proModle.getUnitPrice(), proModle.getQtyOnHand());

                obList.add(TM);
                TbleProduct.setItems(obList);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void setcellValues() {

        colPId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

    }
    public boolean isValied() {
        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.EID, txtPId);
        boolean nameValid = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.NAME, txtPName);
        boolean pricevalid = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PRICE, txtUPrice);
        boolean qtyvalid = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.QTYONHAND, txtQtyOnHand);

        return nameValid && idValied && pricevalid;

    }


    public void btnSaveOnAction(ActionEvent actionEvent) {


        String productId = txtPId.getText();
        String productName = txtPName.getText();
        String unitPrice = txtUPrice.getText();
        String qtyOnHand =txtQtyOnHand.getText();

        Product product = new Product(productId, productName, unitPrice, qtyOnHand);


        try {
            boolean isSaved = ProductRepo.save(product);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "All data saved").show();
                initialize();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "cannot save data").show();
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void idKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PID, txtPId);
    }

    public void nameKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.NAME, txtPName);
    }

    public void priceKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PRICE, txtUPrice);
    }

    public void qtyKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.QTYONHAND, txtQtyOnHand);
    }


    public void btnUpadateOnAction(ActionEvent actionEvent) {

        String productId = txtPId.getText();
        String productName = txtPName.getText();
        String unitPrice = txtUPrice.getText();
        String qtyOnHand =txtQtyOnHand.getText();



        Product product = new Product(productId, productName, unitPrice, qtyOnHand);

        try {
            boolean isUpdated = ProductRepo.update(product);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "product updated!").show();

                initialize();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
        genarateNextProductId();

    }

        private void clearFields() {
        txtSearch.setText("");
        txtPName.setText("");
        txtUPrice.setText("");
        txtQtyOnHand.setText("");

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {


        String id = txtPId.getText();

        try {
            boolean isDeleted = ProductRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "product deleted!").show();

                initialize();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public boolean isValidId(){
        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PID, txtPId);

        return idValied ;
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {

        if(isValidIde()){

        String id = txtPId.getText();

        Product product = ProductRepo.searchById(id);
        if (product != null) {
            txtPId.setText(product.getProductId());
            txtPName.setText(product.getName());
            txtUPrice.setText(product.getUnitPrice());
            txtQtyOnHand.setText(product.getQtyOnHand());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
        } else{

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please enter valid Customer ID correctly.");
            alert.showAndWait();
        }
    }

    public boolean isValidIde(){
        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PID, txtPId);

        return idValied ;
    }

    public void btnAddOnAction(ActionEvent event) throws SQLException {
        String supplierId = (String) cmbSupplierId.getValue();
        String productName = (String) cmbProductName.getValue();
        int qty = Integer.parseInt(txtQty.getText());


        try {
            boolean isPlaced = ProductDetailRepo.updatePrductDetailsTable(supplierId, productName, qty);
            if (isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Product add Successfully!").show();
                cmbSupplierId.setValue(null);
                cmbProductName.setValue(null);
                txtQty.setText("");
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to Add product!").show();
            }
            initialize();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }
    }

    public void btnSearchchOnAction(ActionEvent actionEvent) throws SQLException {
        
        String id = txtSearch.getText();

        Product product = ProductRepo.searchById(id);
        if (product != null) {
            txtPId.setText(product.getProductId());
            txtPName.setText(product.getName());
            txtUPrice.setText(product.getUnitPrice());
            txtQtyOnHand.setText(product.getQtyOnHand());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }

    }
}

