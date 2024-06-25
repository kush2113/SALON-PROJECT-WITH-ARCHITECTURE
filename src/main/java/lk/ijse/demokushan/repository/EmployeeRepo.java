package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.Customer;
import lk.ijse.demokushan.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {

    public static int getEmployeeCount() throws SQLException {

        String sql = "select count(*) from employee ";
        Connection connection =DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }


    public static String getCurrentId() throws SQLException {
        String sql = "SELECT employeeId FROM employee ORDER BY employeeId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String employeeId = resultSet.getString(1);
            return employeeId;
        }
        return null;
    }

    public static List<String> getEmployeeId() throws SQLException {
        String sql = "SELECT employeeId FROM employee";
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


    public static List<String> getEmployeeId(String id) throws SQLException {
        String sql = "SELECT employeeId FROM employee WHERE employeeId = ?";

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

    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getEmployeeId());
        pstm.setObject(2, employee.getName());
        pstm.setObject(3, employee.getContactNumber());
        pstm.setObject(4, employee.getPosition());
        pstm.setObject(5, employee.getSalary());

        return pstm.executeUpdate() > 0;
    }
    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String contactNumber = resultSet.getString(3);
            String position = resultSet.getString(4);
            String salary = resultSet.getString(5);

            Employee employee = new Employee(id, name,  contactNumber, position,salary);
            cusList.add(employee);
        }
        return cusList;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employeeId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String position = resultSet.getString(3);
            String contanctNumber = resultSet.getString(4);
            String salary = resultSet.getString(5);

            Employee employee = new Employee(emp_id, name, position, contanctNumber, salary);

            return employee ;
        }

        return null;
    }
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE employeeId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET employeeName = ?, phoneNumber = ?, position = ?, salary = ?  WHERE employeeId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getName());
        pstm.setObject(2, employee.getContactNumber());
        pstm.setObject(3, employee.getPosition());
        pstm.setObject(4, employee.getSalary());
        pstm.setObject(5, employee.getEmployeeId());
        return pstm.executeUpdate() > 0;
    }
}

