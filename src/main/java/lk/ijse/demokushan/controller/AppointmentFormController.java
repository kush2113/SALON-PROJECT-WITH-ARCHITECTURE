package lk.ijse.demokushan.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lk.ijse.demokushan.Util.Regex;
import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.*;
import lk.ijse.demokushan.model.TM.*;
import lk.ijse.demokushan.repository.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
//import net.sf.jasperreports.view.JasperViewe;
import net.sf.jasperreports.view.JasperViewer;

public class AppointmentFormController {
    public Rectangle rectangal;

    public AnchorPane rootNode;
    public TableView TableAppointment;
    public TableColumn colApId;
    public TableColumn colStatus;
    public TableColumn colDate;
    public TableColumn colTime;
    public TextField txtApId;

    public TextField txtTime;
    public ComboBox cmbProductName;
    public javafx.scene.control.DatePicker txtDatePicker;
    public ComboBox cmbCustomerId;
    public ComboBox cmbEmployeeId;
    public ComboBox cmbHairCutStyle;
    public Label lblQty;
    public TextField txtQty;
    public Label lblQty1;
    public ComboBox cmbStatus;
    public TableColumn colEmployeeId;
    public TableColumn colCustomerId;
    public TableColumn colHaircutId;
    public TextField txtPaymentId;
    public Label lblPaymentId;
    public ComboBox cmbPaymentType;
    public Label lblTotal;
    public Label lblFeedbackId;
    public ComboBox cmbComment;
    public TableView TableFeedback;
    public TableColumn colFId;
    public TableColumn colComment;
    public TableColumn colAppointmentId;
    public Label lblTime;
    public Label lblDate;
    public TableColumn colAction;
    public TextField txtSearch;

    public void initialize() {
        setcellValuese();
        setcellValues();
        loadAllAppointment();
        loadCmbHairStyle();
        loadCmbEmployee();
        loadCmbCustommer();
        loadCmbStatus();
        genarateNextAppointmentId();
        genarateNextPaymentId();
        loadCmbPaymentType();
        genarateNextFeedbackId();
        loadCmbComment();
        loadAllFeedback();
        showSelectedProductDetails();
        setDate();
        setTime();
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Complete", "Incomplete");
        cmbStatus.setItems(statusOptions);

    }
    private void setDate() {
        LocalDate nowDate = LocalDate.now();
        lblDate.setText(String.valueOf(nowDate));
    }

    private void setTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalTime nowTime = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String formattedTime = nowTime.format(formatter);
                    lblTime.setText(formattedTime);
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void showSelectedProductDetails() {
        AppointmentTM selectedUser = (AppointmentTM) TableAppointment.getSelectionModel().getSelectedItem();
        TableAppointment.setOnMouseClicked(event -> showSelectedProductDetails());
        if (selectedUser != null) {
            txtApId.setText(selectedUser.getAppointmentId());
            txtTime.setText(selectedUser.getTime());
            txtDatePicker.setValue(LocalDate.parse(selectedUser.getDate()));
            cmbEmployeeId.setValue(selectedUser.getEmployeeId());
            cmbCustomerId.setValue(selectedUser.getCustomerId());
            cmbHairCutStyle.setValue(selectedUser.getHairCutId());
            cmbStatus.setValue(selectedUser.getStatus());
        }
    }

    private void loadAllFeedback() {

        ObservableList<FeedbackTM> obList = FXCollections.observableArrayList();

        try {
            List<Feedback> feedbackList = FeedbackRepo.getAll();
            for (Feedback fbModle : feedbackList) {

                FeedbackTM TM = new FeedbackTM(fbModle.getFeedbackId(), fbModle.getComment(), fbModle.getAppointmentId());

                obList.add(TM);
                TableFeedback.setItems(obList);

//                JFXButton btnRemove = new JFXButton("Remove");
//                btnRemove.setOnAction((k) -> {
//                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
//                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();
//
//                    if (type.orElse(no) == yes) {
//                        int selectedIndex = TableFeedback.getSelectionModel().getSelectedIndex();
//                        obList.remove(selectedIndex);
//                        TableFeedback.refresh();
//
//                    }
//                });

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnCompleteOnAction(ActionEvent actionEvent) throws SQLException {

        String status = (String) cmbStatus.getValue();

        String paymentId = lblPaymentId.getText();
        String paymentType = (String) cmbPaymentType.getValue();
        String appointmentId = txtApId.getText();
        String amount = lblTotal.getText();

        String fId = lblFeedbackId.getText();
        String comment = (String) cmbComment.getValue();



        Payment payment = new Payment(paymentId, paymentType, appointmentId, amount);
        Feedback feedback = new Feedback(fId, comment, appointmentId);


        List<String> hId = HairCutRepo.getHairCutId((String) cmbHairCutStyle.getValue());
        String hairCutId = hId.get(0);

        try {
            boolean isPlaced = AppointmentRepo.completeAppointment(payment, feedback, hairCutId);
            if (isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Appointment complete Successfully!").show();

                initialize();
                lblTotal.setText("");


            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to complete appointment!").show();
            }

            initialize();
            clearFields();


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }
    }

    private void loadCmbComment() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        obList.add("Bad");
        obList.add("Average");
        obList.add("Good");
        obList.add("Very good");
        cmbComment.setItems(obList);
        cmbComment.setValue("Average");
    }

    private void genarateNextFeedbackId() {
        try {
            String currentId = FeedbackRepo.getCurrentId();

            String nextOrderId = genarateNextFeedbackId(currentId);
            lblFeedbackId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String genarateNextFeedbackId(String currentId) {
        if (currentId != null && currentId.matches("F\\d{3}")) {
            int idNum = Integer.parseInt(currentId.substring(2));
            return "F" + String.format("%03d", idNum + 1);
        } else {
            return "F001";
        }
    }

    private void loadCmbPaymentType() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        obList.add("Card");
        obList.add("Cash");

        cmbPaymentType.setItems(obList);
        cmbPaymentType.setValue("Cash");
    }

    private void genarateNextPaymentId() {
        try {
            String currentId = PaymentRepo.getCurrentId();

            String nextPaymentId = genarateNextPaymentId(currentId);
            lblPaymentId.setText(nextPaymentId);

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

    private void genarateNextAppointmentId() {
        try {
            String currentId = AppointmentRepo.getCurrentId();

            String nextOrderId = genarateNextAppointmentId(currentId);
            txtApId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String genarateNextAppointmentId(String currentId) {
        if (currentId != null && currentId.matches("A\\d{3}")) {
            int idNum = Integer.parseInt(currentId.substring(2));
            return "A" + String.format("%03d", idNum + 1);
        } else {
            return "A001";
        }
    }

    private void loadCmbStatus() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {

            List<String> nameList = AppointmentRepo.getStatus();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbStatus.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCmbEmployee() {


        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = EmployeeRepo.getEmployeeId();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbEmployeeId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCmbHairStyle() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = HairCutRepo.getHairCutNames();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbHairCutStyle.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCmbCustommer() {


        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = CustomerRepo.getCustomerId();

            for (String code : nameList) {
                obList.add(code);
            }
            cmbCustomerId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllAppointment() {

        ObservableList<AppointmentTM> obList = FXCollections.observableArrayList();

        try {
            List<Appointment> appointmentList = AppointmentRepo.getAll();
            for (Appointment apModle : appointmentList) {

                AppointmentTM TM = new AppointmentTM(apModle.getAppointmentId(), apModle.getTime(), apModle.getDate(), apModle.getEmployeeId(), apModle.getCustomerId(), apModle.getHairCutId(), apModle.getStatus());

                obList.add(TM);
                TableAppointment.setItems(obList);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setcellValuese() {

        colApId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colHaircutId.setCellValueFactory(new PropertyValueFactory<>("hairCutId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    private void setcellValues() {

        colFId.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
        colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

    }

    public boolean isValied() {

        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.APID, txtApId);

        return idValied;
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {

        if (isValied()) {

            String appointmentId = txtApId.getText();
            String time = txtTime.getText();
            LocalDate date = txtDatePicker.getValue();
            String employeeId = (String) cmbEmployeeId.getValue();
            String customerId = (String) cmbCustomerId.getValue();
            String hairCutName = (String) cmbHairCutStyle.getValue();
            String status = (String) cmbStatus.getValue();

            if (status == null) {
                new Alert(Alert.AlertType.ERROR, "Please select a status").show();
                return;
            }


            List<String> employeeIdList = EmployeeRepo.getEmployeeId(employeeId);
            System.out.println(employeeIdList.get(0));

            List<String> hairCutIdList = HairCutRepo.getHairCutId(hairCutName);
            System.out.println(hairCutIdList.get(0));

            Appointment appointment = new Appointment(appointmentId, time, date.toString(), employeeIdList.get(0), customerId, hairCutIdList.get(0), status);

            try {
                boolean isSaved = AppointmentRepo.save(appointment);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "All data saved").show();
                    initialize();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "reservation not saved").show();
                }
            } catch (SQLException e) {

                throw new RuntimeException(e);
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please fill in all fields correctly.");
            alert.showAndWait();
        }
    }

    public void idKeyReleaseOnAction(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.demokushan.Util.TextField.APID, txtApId);
    }


    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
        genarateNextAppointmentId();
    }


    private void clearFields() {
        txtSearch.setText("");
        txtTime.setText("");
        txtDatePicker.setValue(null);
        cmbEmployeeId.setValue("");
        cmbCustomerId.setValue("");
        cmbHairCutStyle.setValue("");
        cmbStatus.setValue("");
        lblTotal.setText("");

    }

    public void btnUpadateOnAction(ActionEvent actionEvent) {

        String appointmentId = txtApId.getText();
        String time = txtTime.getText();
        LocalDate date = txtDatePicker.getValue();
        String emloyeeId = (String) cmbEmployeeId.getValue();
        String customerId = (String) cmbCustomerId.getValue();
        String hairCutId = (String) cmbHairCutStyle.getValue();
        String status = (String) cmbStatus.getValue();

        Appointment appointment = new Appointment(appointmentId, time, date.toString(), emloyeeId, customerId, hairCutId, status);

        try {
            boolean isUpdated = AppointmentRepo.update(appointment);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "appointment updated!").show();

                initialize();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {

        if (isValidId()) {

            String id = txtApId.getText();

            try {
                boolean isDeleted = AppointmentRepo.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Appointment deleted!").show();

                    initialize();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Failed");
            alert.setContentText("Please enter valid Appointment ID correctly.");
            alert.showAndWait();
        }
    }

    public boolean isValidId() {
        boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.APID, txtApId);

        return idValied;
    }


    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {

        if(isValidIde()){

        String id = txtApId.getText();

        Appointment appointment = AppointmentRepo.searchById(id);

        if (appointment != null) {

            txtApId.setText(appointment.getAppointmentId());
            txtTime.setText(appointment.getTime());
            txtDatePicker.setValue(LocalDate.parse(appointment.getDate()));
            cmbEmployeeId.setValue(appointment.getEmployeeId());
            cmbCustomerId.setValue(appointment.getCustomerId());
            cmbHairCutStyle.setValue(appointment.getHairCutId());
            cmbStatus.setValue(appointment.getStatus());

            lblTotal.setText(HairCutRepo.getHairCutPrice(appointment.getHairCutId()));

        } else {
            new Alert(Alert.AlertType.INFORMATION, "appointment not found!").show();
        }
    } else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Validation Failed");
        alert.setContentText("Please enter valid Appointment ID correctly.");
        alert.showAndWait();
    }
}

public boolean isValidIde(){
    boolean idValied = Regex.setTextColor(lk.ijse.demokushan.Util.TextField.APID, txtApId);

    return idValied ;
}

    public void btnBillOnAction(ActionEvent actionEvent) throws SQLException, JRException {

        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/SalonOneBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String, Object> data = new HashMap<>();
        data.put("appointmentId", txtApId.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint, false);



        }

    public void btnSearchchOnAction(ActionEvent actionEvent) throws SQLException {

        String id = txtSearch.getText();

        Appointment appointment = AppointmentRepo.searchById(id);

        if (appointment != null) {

            txtApId.setText(appointment.getAppointmentId());
            txtTime.setText(appointment.getTime());
            txtDatePicker.setValue(LocalDate.parse(appointment.getDate()));
            cmbEmployeeId.setValue(appointment.getEmployeeId());
            cmbCustomerId.setValue(appointment.getCustomerId());
            cmbHairCutStyle.setValue(appointment.getHairCutId());
            cmbStatus.setValue(appointment.getStatus());

            lblTotal.setText(HairCutRepo.getHairCutPrice(appointment.getHairCutId()));

        } else {
            new Alert(Alert.AlertType.INFORMATION, "appointment not found!").show();
        }
    }
}
