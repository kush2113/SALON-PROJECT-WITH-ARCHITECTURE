package lk.ijse.demokushan.dao.custom.impl;

import lk.ijse.demokushan.dao.SQLUtil;
import lk.ijse.demokushan.dao.custom.ProductDetailsDAO;
import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.entity.Product_detail;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductDetailsDAOImpl implements ProductDetailsDAO {
    @Override
    public  boolean updatePrductDetailsTable(String supplierId, String productName, int qty) throws SQLException {
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

    public boolean updateProductQtyOnHand(String pName, int qty) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE product SET qtyOnHand = qtyOnHand + ? WHERE productName = ?", qty, pName);
    }

    public boolean addProductDetails(String sId, String pName, int qty) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO productDetail VALUES(?, ?, ?)",sId,getProductId(sId, pName, qty),qty);
    }

    @Override
    public String getProductId(String sId, String pName, int qty) throws SQLException, ClassNotFoundException {

        return SQLUtil.execute("SELECT productId from product WHERE productName = ?",pName);
    }

    @Override
    public boolean setAssociate(Product_detail productDetail) throws SQLException, ClassNotFoundException {

        return SQLUtil.execute("INSERT INTO product_detail VALUES(?, ?)",productDetail.getSname(),productDetail.getPname());
    }
}
