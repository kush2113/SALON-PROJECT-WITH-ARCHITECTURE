package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.TM.MostAppointmentTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashBoardRepo {

    public static List<MostAppointmentTM> getMostSellItem() throws SQLException {
        List<MostAppointmentTM> sellItems = new ArrayList<>();
        String sql = "SELECT c.customerId, c.customerName, COUNT(a.appointmentId) AS visitCount " +
                "FROM customer c LEFT JOIN appointment a ON c.customerId = a.customerId " +
                "GROUP BY c.customerId, c.customerName";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {

            while (resultSet.next()) {
                String id = resultSet.getString("customerId");
                String name = resultSet.getString("customerName");
                int visitCount = resultSet.getInt("visitCount");

                MostAppointmentTM mostAppointmentTM = new MostAppointmentTM(id, name, visitCount);
                sellItems.add(mostAppointmentTM);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately, e.g., log it
        }

        return sellItems;
    }

//    public static List<MostAppointmentTM> getMostSellItem() throws SQLException {
//            String sql = "SELECT c.customerId, c.customerName, COUNT(a.appointmentId) AS visitCount FROM customer c LEFT JOIN appointment a ON c.customerId = a.customerId GROUP BY c.customerId, c.customerName";
//
//            PreparedStatement pstm = DbConnection.getInstance().getConnection()
//                    .prepareStatement(sql);
//
//            ResultSet resultSet = pstm.executeQuery();
//
//            List<MostAppointmentTM> sellItem = new ArrayList<>();
//
//            while (resultSet.next()) {
//                String id = resultSet.getString(1);
//                String name = resultSet.getString(2);
//                int vistCount = resultSet.getInt(3);
//
//                MostAppointmentTM mostSellItem = new MostAppointmentTM(id, name, vistCount);
//                sellItem.add(mostSellItem);
//            }
//            return sellItem;
//        }
    }

