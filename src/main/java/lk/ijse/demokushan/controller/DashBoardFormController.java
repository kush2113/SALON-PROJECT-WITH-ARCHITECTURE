package lk.ijse.demokushan.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lk.ijse.demokushan.dao.DAOFactory;
import lk.ijse.demokushan.dao.custom.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashBoardFormController {

    @FXML
    private JFXButton AppointmentBtn;

    @FXML
    private JFXButton CustomerBtn;

    @FXML
    private JFXButton EmployeeBtn;

    @FXML
    private JFXButton HairCutBtn;

    @FXML
    private JFXButton HairCutBtn1;

    @FXML
    private JFXButton PaymentBtn;

    @FXML
    private PieChart PieChart;

    @FXML
    private JFXButton ProductBtn;

    @FXML
    private JFXButton SupplierBtn;

    @FXML
    private Label lblApCount;

    @FXML
    private Label lblAppointment;

    @FXML
    private Label lblCompleteAppointment;

    @FXML
    private Label lblCompleteCount;

    @FXML
    private Label lblCount;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblEmployee;

    @FXML
    private Label lblEmployeeCount;

    @FXML
    private Label lblFullPayment;

    @FXML
    private Label lblFullPaymentCount;

    @FXML
    private Label lblInCompleteAppointment2;

    @FXML
    private Label lblInCompleteCount;

    @FXML
    private Label lblSupplier;

    @FXML
    private Label lblSupplierCount;

    @FXML
    private Label lblTime;

    @FXML
    private Rectangle rectangal;

    @FXML
    private Rectangle rectangal1;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane root1;

    @FXML
    private AnchorPane root2;

    @FXML
    private AnchorPane root21;

    @FXML
    private AnchorPane root211;

    @FXML
    private AnchorPane root2111;

    @FXML
    private AnchorPane root21111;

    @FXML
    private AnchorPane root212;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private Text txt;

    private int cCount;
    private int aCount;
    private int eCount;

    private  int sCount;




    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    AppointmentDAO appointmentDAO = (AppointmentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.APPOINTMENT);
    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.EMPLOYEE);
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);


    public void initialize() {

        startTypingAnimation();

        setTime();
        setDate();
        pieChartConnect();
        animatePieChart();

        int completeCount = 0;
        int incompleteCount = 0;
        double fullPaymentCount = 0;

        try {
            cCount = CustomerRepo.getCustomerCount();
            completeCount = AppointmentRepo.getCompleteAppointmentCount();
            incompleteCount = AppointmentRepo.getIncompleteAppointmentCount();
            aCount = completeCount + incompleteCount;
            eCount = EmployeeRepo.getEmployeeCount();
            sCount = SupplierRepo.getSupplierCount();

            fullPaymentCount = PaymentRepo.getAllPaymentCount();


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setSupplierCount(sCount);
        setCount(cCount);
        setAppointmentCount(aCount);
        setEmployeeCount(eCount);
        setCompleteAppointmentCount(completeCount);
        setIncompleteAppointmentCount(incompleteCount);
        setFullPaymentCount(fullPaymentCount);

    }

    private void setCount(int cCount) {
        lblCount.setText(String.valueOf(cCount));
    }

    private void setSupplierCount(int sCount) {
        lblSupplierCount.setText(String.valueOf(sCount));
    }

    private void setAppointmentCount(int aCount) {
        lblApCount.setText(String.valueOf(aCount));
    }

    private void setCompleteAppointmentCount(int completeCount) {
        lblCompleteCount.setText(String.valueOf(completeCount));
    }

    private void setIncompleteAppointmentCount(int incompleteCount) {
        lblInCompleteCount.setText(String.valueOf(incompleteCount));
    }

    private void setEmployeeCount(int aCount) {
        lblEmployeeCount.setText(String.valueOf(eCount));

    }

    private void setFullPaymentCount(double fullPaymentCount) {

        lblFullPaymentCount.setText(String.valueOf(fullPaymentCount));
    }

    private void startTypingAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {

            if (currentIndex < fullText.length()) {
                txt.setText(fullText.substring(0, currentIndex + 1));
                currentIndex++;
            } else {

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    public void pieChartConnect() {

        List<MostAppointmentTM> itemList = null;
        try {
            itemList = DashBoardRepo.getMostSellItem();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Customer item;
        for (MostAppointmentTM sellItem : itemList) {
            try {
                item = CustomerRepo.searchById(sellItem.getCustomerId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data(item.getCustomerId(), sellItem.getVisitCount())
                    );

            PieChart.getData().addAll(pieChartData);

        }
    }

    private void animatePieChart() {
        PieChart.setOpacity(0);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(PieChart.opacityProperty(), 1))
        );

        timeline.setOnFinished(event -> setPieChartData());

        timeline.play();
    }

    private void setPieChartData() {

        for (PieChart.Data data : PieChart.getData()) {

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(data.getNode().scaleXProperty(), 1),
                            new KeyValue(data.getNode().scaleYProperty(), 1))
            );
            timeline.play();
        }
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





    @FXML
    void btnAppointmentOnAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/appointmnt_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/customer_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);

    }

    @FXML
    void btnDashBoardonAction(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/view/home_page.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);

    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/employee_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);

    }

    @FXML
    void btnHariCutonAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/haircut_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);
        ;
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/payment_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);
    }

    @FXML
    void btnProductOnAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/product_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);

    }

    @FXML
    void btnProfileOnAction(ActionEvent event) {

    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) throws IOException {

        URL resource = getClass().getResource("/view/supplier_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        AnchorPane load = fxmlLoader.load();
        root1.getChildren().clear();
        root1.getChildren().add(load);

    }

}
