package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HairCutRepo {




    public static String getHairCutPrice(String style) throws SQLException {
        String sql = "SELECT price FROM hairCut WHERE hairCutStyle = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, style);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String price = resultSet.getString(1);
            return price;
        }
        return null;
    }
    public static boolean placeHairCut(AddNewHairCut po) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isHairCutUpdate = addNewHairCut(po.getHairCut());
            System.out.println(isHairCutUpdate);
            System.out.println("isHairCutUpdate");
            if (isHairCutUpdate) {
                System.out.println("isHairCutDetailsUpdate");
                boolean isHairCutDetailsUpdate = saveHairCutDetailsTable(po.getHdList());
                System.out.println("isHairCutDetailsUpdate");
                if (isHairCutDetailsUpdate) {
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

    static boolean saveHairCutDetailsTable(List<HairCutDetails> hairCutTableDetails) throws SQLException {
        String sql = "INSERT INTO hairCutDetails VALUES (?, ?, ?)";


        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            for (HairCutDetails details : hairCutTableDetails) {
                pstm.setString(1, details.getProductId());
                pstm.setString(2, details.getHairCutId());
                pstm.setInt(3, details.getProductQty());
                pstm.addBatch();

            }
            int[] updateCounts = pstm.executeBatch();
            return Arrays.stream(updateCounts).allMatch(count -> count > 0);
        }
    }

    static boolean addNewHairCut(HairCut hairCutTableDetails) throws SQLException {
        String sql = "INSERT INTO hairCut VALUES  (?, ? ,?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, hairCutTableDetails.getHairCutId());
        pstm.setString(2, hairCutTableDetails.getStyle());
        pstm.setDouble(3, hairCutTableDetails.getPrice());

        return pstm.executeUpdate() > 0;

    }



    public static String getCurrentId() throws SQLException {
        String sql = "SELECT hairCutId FROM hairCut ORDER BY hairCutId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String hairCutId = resultSet.getString(1);
            return hairCutId;
        }
        return null;
    }


    public static  List<String> getProductDetails(String name) throws SQLException {
        String sql = "SELECT productId, unitPrice, qtyOnHand FROM product WHERE productName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);


        ResultSet resultSet = pstm.executeQuery();
        List<String> detailList = new ArrayList<>();
        if (resultSet.next()) {
            detailList.add(resultSet.getString(1));
            detailList.add(resultSet.getString(2));
            detailList.add(resultSet.getString(3));
        }
        return detailList;
    }

    public static List<String> getProductNames() throws SQLException { //appointment
        String sql = "SELECT productName FROM product";
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

    public static List<String> getHairCutNames() throws SQLException { //appointment
        String sql = "SELECT hairCutStyle FROM hairCut";
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

    public static List<String> getHairCutId(String id) throws SQLException {
        String sql = "SELECT hairCutId FROM hairCut WHERE hairCutStyle = ?";

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

    public static boolean save(HairCut hairCut) throws SQLException {
        String sql = "INSERT INTO hairCut VALUES(?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, hairCut.getHairCutId());
        pstm.setObject(2, hairCut.getStyle());
        pstm.setObject(3, hairCut.getPrice());

        return pstm.executeUpdate() > 0;
    }

    public static List<HairCut> getAll() throws SQLException {
        String sql = "SELECT * FROM hairCut";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<HairCut> hairCutList = new ArrayList<>();

        while (resultSet.next()) {
            String hc_Id = resultSet.getString(1);
            String style = resultSet.getString(2);
            double price = Double.parseDouble(resultSet.getString(3));

            HairCut hairCut = new HairCut(hc_Id, style, price);
            hairCutList.add(hairCut);
        }
        return hairCutList;
    }

    public static boolean update(HairCut hairCut) throws SQLException {
        String sql = "UPDATE haircut SET  hairCutStyle = ?, price = ? WHERE hairCutId  = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, hairCut.getStyle());
        pstm.setObject(2, hairCut.getPrice());
        pstm.setObject(3, hairCut.getHairCutId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM haircut WHERE hairCutId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static HairCut searchById(String id) throws SQLException {
        String sql = "SELECT * FROM hairCut WHERE hairCutId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String hc_id = resultSet.getString(1);
            String hc_style = resultSet.getString(2);
            double price = Double.parseDouble(resultSet.getString(3));

            HairCut hairCut = new HairCut(hc_id, hc_style, price);

            return hairCut;
        }

        return null;
    }

    public static ResultSet getProductId(String name) throws SQLException {
        String sql = "SELECT productId FROM product WHERE productName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, name);


        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        return resultSet;

    }
}