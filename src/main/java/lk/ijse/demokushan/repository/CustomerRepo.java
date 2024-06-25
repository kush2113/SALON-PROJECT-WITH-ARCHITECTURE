package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT customerId FROM customer ORDER BY customerId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String customerId = resultSet.getString(1);
            return customerId;
        }
        return null;
    }


    public static boolean save(Customer customer) throws SQLException {
            String sql = "INSERT INTO customer VALUES(?, ?, ?, ?, ?, ?)";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, customer.getCustomerId());
            pstm.setObject(2, customer.getName());
            pstm.setObject(3, customer.getPhoneNumber());
            pstm.setObject(4, customer.getAddress());
            pstm.setObject(5, customer.getEmail());
            pstm.setObject(6, customer.getStatus());

            return pstm.executeUpdate() > 0;
        }

    public static int getCustomerCount() throws SQLException {

        String sql = "select count(*) from customer ";
        Connection connection =DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }



        public static List<Customer> getAll() throws SQLException {
            String sql = "SELECT * FROM customer";

            PreparedStatement pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            List<Customer> cusList = new ArrayList<>();

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String phoneNumber = resultSet.getString(3);
                String address = resultSet.getString(4);
                String email = resultSet.getString(5);
                String status = resultSet.getString(6);

                Customer customer = new Customer(id, name, phoneNumber, address ,email,status);
                cusList.add(customer);
            }
        return cusList;
    }
    public static Customer searchById(String id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customerId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String customerId = resultSet.getString(1);
            String customerName = resultSet.getString(2);
            String phoneNumber = resultSet.getString(3);
            String address = resultSet.getString(4);
            String email = resultSet.getString(5);
            String status = resultSet.getString(6);

            Customer customer = new Customer(customerId, customerName, phoneNumber, address, email,status);

            return customer;
        }

        return null;
    }
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE customerId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET customerName = ?,  phoneNumber = ?,address = ?, email = ? , status = ? WHERE customerId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer.getName());
        pstm.setObject(2, customer.getPhoneNumber());
        pstm.setObject(3, customer.getAddress());
        pstm.setObject(4, customer.getEmail());
        pstm.setObject(5, customer.getStatus());
        pstm.setObject(6, customer.getCustomerId());

        return pstm.executeUpdate() > 0;


    }

    public static List<String> getCustomerId() throws SQLException {

        String sql = "SELECT customerId FROM customer";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
    public static List<String> getCustomerId(String id) throws SQLException {
        String sql = "SELECT customerId FROM customer WHERE customerId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
    public static List<String> getStatus() throws SQLException {
        String sql = "SELECT status FROM customer";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
    public static List<String> getStatus(String status) throws SQLException {
        String sql = "SELECT status FROM customer WHERE status = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, status);

        ResultSet resultSet = pstm.executeQuery();

        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
}

