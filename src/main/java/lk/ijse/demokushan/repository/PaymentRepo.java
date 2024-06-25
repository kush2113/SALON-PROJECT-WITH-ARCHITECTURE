package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.Customer;
import lk.ijse.demokushan.model.Employee;
import lk.ijse.demokushan.model.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepo {



    public static String getCurrentId() throws SQLException {
        String sql = "SELECT paymentId FROM payment ORDER BY paymentId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String paymentId = resultSet.getString(1);
            return paymentId;
        }
        return null;
    }


    public static boolean save(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, payment.getPaymentId());
        pstm.setObject(2, payment.getPaymentType());
        pstm.setObject(3, payment.getAppintmentId());
        pstm.setObject(4, payment.getAmount());


        return pstm.executeUpdate() > 0;
    }

    public static List<Payment> getAll() throws SQLException {
        String sql = "SELECT * FROM payment";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Payment> paymentList = new ArrayList<>();

        while (resultSet.next()) {
            String paymentId = resultSet.getString(1);
            String paymentType = resultSet.getString(2);
            String appointmetId = resultSet.getString(3);
            String amount = resultSet.getString(4);


            Payment payment = new Payment(paymentId, paymentType,appointmetId, amount);
            paymentList.add(payment);
        }
        return paymentList;
    }
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM payment WHERE paymentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static Payment searchById(String id) throws SQLException {
        String sql = "SELECT * FROM payment WHERE paymentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String paymentId = resultSet.getString(1);
            String paymentType = resultSet.getString(2);
            String appointmentId = resultSet.getString(3);
            String amount = resultSet.getString(4);




            Payment payment = new Payment(paymentId, paymentType,appointmentId, amount);

            return payment ;
        }

        return null;
    }

    public static double getAllPaymentCount() throws SQLException {
        String sql = "SELECT SUM(amount) FROM payment";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble(1) ;
        }
        return 0;
    }
}
