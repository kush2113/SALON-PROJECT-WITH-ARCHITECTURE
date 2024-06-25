package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.AddNewHairCut;
import lk.ijse.demokushan.model.Customer;
import lk.ijse.demokushan.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo {

    public static boolean productQtyUpdate(String hairCutId) throws SQLException {
        String sql = "SELECT p.productId, hd.productQty FROM product p JOIN hairCutDetails hd ON p.productId = hd.productId WHERE hd.hairCutId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, hairCutId);

        ResultSet resultSet = pstm.executeQuery();
        boolean updated = false;
        while (resultSet.next()) {
            String productId = resultSet.getString("productId");
            int productQty = resultSet.getInt("productQty");

            boolean isUpdateQty = updateQty(productId, productQty);

            updated = true; // Placeholder for actual update logic
        }
        return updated;
    }




    static boolean updateQty(String productId, int productQty) throws SQLException {
        String sql = "UPDATE product SET qtyOnHand = qtyOnHand - ? WHERE productId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, String.valueOf(productQty));
        pstm.setString(2, productId);

        return pstm.executeUpdate() > 0;
    }


    public static String getCurrentId() throws SQLException {
        String sql = "SELECT productId FROM product ORDER BY productId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String productId = resultSet.getString(1);
            return productId;
        }
        return null;
    }


    public static boolean save(Product product) throws SQLException {
        String sql = "INSERT INTO product VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, product.getProductId());
        pstm.setObject(2, product.getName());
        pstm.setObject(3, product.getUnitPrice());
        pstm.setObject(4, product.getQtyOnHand());


        return pstm.executeUpdate() > 0;
    }

    public static List<Product> getAll() throws SQLException {
        String sql = "SELECT * FROM product";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Product> productList = new ArrayList<>();

        while (resultSet.next()) {
            String pro_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String unitPrice = resultSet.getString(3);
            String qtyOnHand = resultSet.getString(4);




            Product product = new Product(pro_id, name, unitPrice ,qtyOnHand);
            productList.add(product);
        }
        return productList;
    }
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM product WHERE productId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Product product) throws SQLException {
        String sql = "UPDATE product SET productName = ?, unitPrice = ?, qtyOnHand = ? WHERE productId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, product.getName());
        pstm.setObject(2, product.getUnitPrice());
        pstm.setObject(3, product.getQtyOnHand());
        pstm.setObject(4, product.getProductId());

        return pstm.executeUpdate() > 0;
    }

    public static Product searchById(String id) throws SQLException {
        String sql = "SELECT * FROM product WHERE productId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String productId = resultSet.getString(1);
            String productName = resultSet.getString(2);
            String unitPrice = resultSet.getString(3);
            String qtyOnHand = resultSet.getString(4);

            Product product = new Product(productId, productName, unitPrice, qtyOnHand);

            return product;
        }

        return null;
    }

}

