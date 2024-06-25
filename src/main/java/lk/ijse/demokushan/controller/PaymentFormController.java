package lk.ijse.demokushan.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.demokushan.Util.Regex;
import lk.ijse.demokushan.model.Customer;
import lk.ijse.demokushan.model.Employee;
import lk.ijse.demokushan.model.Payment;
import lk.ijse.demokushan.model.TM.CustomerTM;
import lk.ijse.demokushan.model.TM.EmployeeTM;
import lk.ijse.demokushan.model.TM.PaymentTM;
import lk.ijse.demokushan.repository.*;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

//import static jdk.internal.agent.Agent.getText;

public class PaymentFormController {
    public AnchorPane rootNode;
    public TextField txtId;
    public TextField txtTyp;
    public TextField txtAmount;

    public TextField txtCId;
    public TextField TxtEId;
    public TableColumn colPId;
    public TableColumn colPType;
    public TableColumn colAmount;

    public TableView tablePayment;
    public javafx.scene.control.DatePicker DatePicker;
    public javafx.scene.control.DatePicker txtDatePicker;
    public ComboBox cmbAppointmentId;
    public TableColumn colAppointmentId;

    public Rectangle rectangal;
    public TextField txtSearch;

    public  void initialize(){
        setcellValues();
        loadAllPayment();
        loadCmbAppointment();
        genarateNextPaymentId();
        showSelectedProductDetails();
    }

    private void showSelectedProductDetails() {
        PaymentTM selectedUser = (PaymentTM) tablePayment.getSelectionModel().getSelectedItem();
        tablePayment.setOnMouseClicked(event -> showSelectedProductDetails());
        if (selectedUser != null) {

            txtId.setText(selectedUser.getPaymentId());
            txtTyp.setText(selectedUser.getPaymentType());
            cmbAppointmentId.setValue(selectedUser.getAppointmentId());
            txtAmount.setText(selectedUser.getAmount());

        }
    }

    private void genarateNextPaymentId() {
        try {
            String currentId = PaymentRepo.getCurrentId();

            String nextOrderId = genarateNextPaymentId(currentId);
            txtId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String genarateNextPaymentId(String currentId) {
        if (currentId != null && currentId.matches("PA\\d{3}")) {
            int idNum = Integer.parseInt(currentId.substring(2));
            return "PA" + String.format("%03d", idNum + 1);
        } else {
            return "PA001";
        }
    }

    private void loadCmbAppointment() {


        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = AppointmentRepo.getAppointmentId();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbAppointmentId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private  void loadAllPayment(){

        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();

        try {
            List<Payment> paymentsList = PaymentRepo.getAll();
            for (Payment payModle : paymentsList){

                PaymentTM TM = new PaymentTM(payModle.getPaymentId(),payModle.getPaymentType(),payModle.getAppintmentId(),payModle.getAmount());

                obList.add(TM);
                tablePayment.setItems(obList);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private  void setcellValues(){

        colPId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

    public boolean isValied() {
        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PAID, txtId);
        boolean typeValid = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.TYPE, txtTyp);
        boolean amountvalid = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.AMOUNT, txtAmount);


        return typeValid && idValied && amountvalid;

    }

    @FXML
    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {

        if (isValied()) {

            String paymentId = txtId.getText();
            String paymentType = txtTyp.getText();
            String appointmentId = (String) cmbAppointmentId.getValue();
            String amount = txtAmount.getText();

            List<String> appintmentIdList = AppointmentRepo.getAppointmentId(appointmentId);
            System.out.println(appintmentIdList.get(0));
            Payment payment = new Payment(paymentId, paymentType, appintmentIdList.get(0), amount);

            try {
                boolean isSaved = PaymentRepo.save(payment);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "payment saved!").show();

                    initialize();
                    clearFields();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }
    public void idKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PAID, txtId);
    }

    public void typeKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.TYPE, txtTyp);
    }

    public void amountKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.AMOUNT, txtAmount);
    }


    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
        genarateNextPaymentId();
    }

    private void clearFields() {

        txtSearch.setText("");
        txtTyp.setText("");
        cmbAppointmentId.setValue("");
        txtAmount.setText("");

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {

        if(isValidId()) {

            String id = txtId.getText();

            try {
                boolean isDeleted = PaymentRepo.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "payment deleted!").show();

                    initialize();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please enter valid Payment ID correctly.");
            alert.showAndWait();
        }
    }
    public boolean isValidId() {

        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PAID, txtId);

        return idValied;
    }


    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {

        if(isValidIde()){

        String id = txtId.getText();

        Payment payment = PaymentRepo.searchById(id);
        if (payment != null) {
            txtId.setText(payment.getPaymentId());
            txtTyp.setText(payment.getPaymentType());
            cmbAppointmentId.setValue(payment.getAppintmentId());
            txtAmount.setText(payment.getAmount());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "payment not found!").show();
        }
    }else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Validation Failed");
        alert.setContentText("Please enter valid Payment ID correctly.");
        alert.showAndWait();
    }
}
public boolean isValidIde() {

    boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.PAID, txtId);

    return idValied;
}

    public void btnSearchchOnAction(ActionEvent actionEvent) throws SQLException {

        String id = txtSearch.getText();

        Payment payment = PaymentRepo.searchById(id);
        if (payment != null) {
            txtId.setText(payment.getPaymentId());
            txtTyp.setText(payment.getPaymentType());
            cmbAppointmentId.setValue(payment.getAppintmentId());
            txtAmount.setText(payment.getAmount());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "payment not found!").show();
        }

    }
}
