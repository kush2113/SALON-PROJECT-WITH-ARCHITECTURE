package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.Customer;
import lk.ijse.demokushan.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {

    public static int getSupplierCount() throws SQLException {

        String sql = "select count(*) from supplier ";
        Connection connection =DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }



    public static List<String> getSupplierId() throws SQLException {
        String sql = "SELECT supplierID FROM supplier";
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


    public static String getCurrentId() throws SQLException {
        String sql = "SELECT supplierId FROM supplier ORDER BY supplierId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String supplierId = resultSet.getString(1);
            return supplierId;
        }
        return null;
    }


    public static boolean save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, supplier.getSupplierId());
        pstm.setObject(2, supplier.getName());
        pstm.setObject(3, supplier.getNic());
        pstm.setObject(4, supplier.getPhoneNumber());


        return pstm.executeUpdate() > 0;
    }

    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supplierList = new ArrayList<>();

        while (resultSet.next()) {
            String sId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String phoneNumber = resultSet.getString(4);


            Supplier supplier = new Supplier(sId, name, nic, phoneNumber);
            supplierList.add(supplier);
        }
        return supplierList;
    }
    public static boolean delete(String nic) throws SQLException {
        String sql = "DELETE FROM supplier WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        return pstm.executeUpdate() > 0;
    }
    public static Supplier searchByNic(String nic) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE NIC = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String sup_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String nic1 = resultSet.getString(3);
            String phoneNumber = resultSet.getString(4);

            Supplier supplier = new Supplier(sup_id, name, nic1, phoneNumber);

            return supplier;
        }

        return null;
    }

    public static List<String> getNames() throws SQLException {
        String sql = "SELECT s_name FROM supplier";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> nameList = new ArrayList<>();
        while (resultSet.next()) {
            nameList.add(resultSet.getString(1));
        }
        return nameList;
    }

    public static boolean update(Supplier supplier) throws SQLException {

        String sql = "UPDATE supplier SET supplierName = ?, NIC = ?, phoneNumber = ?  WHERE supplierId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,supplier.getName());
        pstm.setObject(2, supplier.getNic());
        pstm.setObject(3, supplier.getPhoneNumber());
        pstm.setObject(4, supplier.getSupplierId());

        return pstm.executeUpdate() > 0;

    }
}
