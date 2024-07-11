package lk.ijse.demokushan.dao.custom;

import lk.ijse.demokushan.dao.CrudDAO;
import lk.ijse.demokushan.dao.SuperDAO;
import lk.ijse.demokushan.entity.Product_detail;

import java.sql.SQLException;

public interface ProductDetailsDAO extends SuperDAO {

    boolean updatePrductDetailsTable(String supplierId, String productName, int qty) throws SQLException;

    String getProductId(String sId, String pName, int qty) throws SQLException, ClassNotFoundException;

    boolean setAssociate(Product_detail productDetail) throws SQLException, ClassNotFoundException;
}
