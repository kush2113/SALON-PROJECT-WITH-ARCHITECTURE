package lk.ijse.demokushan.bo.custom.impl;

import lk.ijse.demokushan.bo.custom.ProductDetailsBO;
import lk.ijse.demokushan.dao.DAOFactory;
import lk.ijse.demokushan.dao.custom.ProductDetailsDAO;
import lk.ijse.demokushan.entity.Product_detail;

import java.sql.SQLException;

public class ProductDetailsBOImpl implements ProductDetailsBO {


    ProductDetailsDAO productDetailsDAO = (ProductDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PRODUCTDETAILS);


    @Override
    public  boolean updatePrductDetailsTable(String supplierId, String productName, int qty) throws SQLException{
        return productDetailsDAO.updatePrductDetailsTable(supplierId, productName, qty);
    }


    @Override
    public String getProductId(String sId, String pName, int qty) throws SQLException, ClassNotFoundException {
        return productDetailsDAO.getProductId(sId, pName, qty);
    }

    @Override
    public boolean setAssociate(Product_detail productDetail) throws SQLException, ClassNotFoundException {
        return productDetailsDAO.setAssociate(productDetail);
    }
}