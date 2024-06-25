package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.AddNewHairCut;
import lk.ijse.demokushan.model.HairCut;
import lk.ijse.demokushan.model.Product_detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailRepo {



    public static boolean updatePrductDetailsTable(String supplierId, String productName, int qty) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isProductDetailsTable = addProductDetails(supplierId, productName, qty);
            System.out.println(isProductDetailsTable);
            System.out.println("isProductDetailsTable");

            if (isProductDetailsTable) {
                System.out.println("isHairCutDetailsUpdate");
                boolean isUpdateProductQtyOnHand = updateProductQtyOnHand(productName, qty);
                System.out.println("isHairCutDetailsUpdate");
                System.out.println(isUpdateProductQtyOnHand);
                if (isUpdateProductQtyOnHand) {
                    System.out.println("commit");
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }


    }

    public static boolean updateProductQtyOnHand(String pName, int qty) throws SQLException {
        String sql = "UPDATE product SET qtyOnHand = qtyOnHand + ? WHERE productName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, qty);
        pstm.setObject(2, pName);


        return pstm.executeUpdate() > 0;
    }

    public static boolean addProductDetails(String sId, String pName, int qty) throws SQLException {

        String pID = getProductId(sId, pName, qty);


        String sql = "INSERT INTO productDetail VALUES(?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, sId);
        pstm.setObject(2, pID);
        pstm.setObject(3, qty);

        return pstm.executeUpdate() > 0;
    }



    public static String getProductId(String sId, String pName, int qty) throws SQLException {
        String sql = "SELECT productId from product WHERE productName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, pName);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String pId = resultSet.getString(1);
            System.out.println(pId);

            return pId;
        }

        return null;
    }

    public static boolean setAssociate(Product_detail productDetail) throws SQLException {
        String sql = "INSERT INTO product_detail VALUES(?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, productDetail.getSname());
        pstm.setObject(2, productDetail.getPname());


        return pstm.executeUpdate() > 0;
    }
}
