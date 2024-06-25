package lk.ijse.demokushan.repository;

import lk.ijse.demokushan.db.DbConnection;
import lk.ijse.demokushan.model.Appointment;
import lk.ijse.demokushan.model.Feedback;
import lk.ijse.demokushan.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepo {

    public static boolean completeAppointment(Payment payment, Feedback feedback, String hairCutId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {

            boolean isPaymentUpdate = PaymentRepo.save(payment);
            System.out.println(isPaymentUpdate);
            System.out.println("isPaymentUpdate");

            if (isPaymentUpdate) {
                boolean isFeedbackUpdate = FeedbackRepo.save(feedback);
                System.out.println(isFeedbackUpdate);
                System.out.println("isFeedbackUpdate");

                if (isFeedbackUpdate) {
                    boolean isProductQtyUpdate = ProductRepo.productQtyUpdate(hairCutId);
                    System.out.println(isProductQtyUpdate);

                    if (isProductQtyUpdate) {
                        boolean isStatusUpdate = updateAppointmentStatus(feedback.getAppointmentId());

                        if (isStatusUpdate) {
                            System.out.println("commit");
                            connection.commit();
                            return true;
                        }
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
//            return pstm.executeUpdate() > 0;
        }


    }



    static boolean updateAppointmentStatus(String id) throws SQLException {
            String sql = "UPDATE appointment SET status = 'Complete' WHERE appointmentId = ?";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, id);

            return pstm.executeUpdate() > 0;

    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT appointmentId FROM appointment ORDER BY appointmentId DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String appointmentId = resultSet.getString(1);
            return appointmentId;
        }
        return null;
    }

    public static List<String> getAppointmentId() throws SQLException{
    String sql = "SELECT appointmentId FROM appointment";
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

    public static boolean save(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointment VALUES(?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, appointment.getAppointmentId());
        pstm.setObject(2, appointment.getTime());
        pstm.setObject(3, appointment.getDate());
        pstm.setObject(4, appointment.getEmployeeId());
        pstm.setObject(5, appointment.getCustomerId());
        pstm.setObject(6, appointment.getHairCutId());
        pstm.setObject(7, appointment.getStatus());

        return pstm.executeUpdate() > 0;

    }

    public static int getCompleteAppointmentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointment WHERE status = 'Complete'";

        Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        return 0;
    }

    public static int getIncompleteAppointmentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointment WHERE status = 'Incomplete'";

       Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        return 0;
    }

















//    public static int getAppointmentCount() throws SQLException {
//
//        String sql = "select count(*) from appointment ";
//
//        Connection connection =DbConnection.getInstance().getConnection();
//        PreparedStatement pstm = connection.prepareStatement(sql);
//        ResultSet resultSet = pstm.executeQuery();
//
//        if(resultSet.next()) {
//            return resultSet.getInt(1);
//        }
//        return 0;
//    }


    public static List<Appointment> getAll() throws SQLException {
        String sql = "SELECT * FROM appointment";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Appointment> appointmentList = new ArrayList<>();

        while (resultSet.next()) {
            String appointmentId = resultSet.getString(1);
            String time = resultSet.getString(2);
            String date = resultSet.getString(3);
            String employeeId = resultSet.getString(4);
            String customerId = resultSet.getString(5);
            String hirCutId = resultSet.getString(6);
            String status = resultSet.getString(7);


            Appointment appointment = new Appointment(appointmentId, time, date, employeeId, customerId, hirCutId, status);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM appointment WHERE appointmentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getAppointmentId(String appointmentId) throws SQLException {
        String sql = "SELECT appointmentId FROM appointment WHERE appointmentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, appointmentId);

        ResultSet resultSet = pstm.executeQuery();

        List<String> idList = new ArrayList<>();
        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static Appointment searchById(String id) throws SQLException {
        String sql = "SELECT * FROM appointment WHERE appointmentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String appoinmentId = resultSet.getString(1);
            String time = resultSet.getString(2);
            String date = resultSet.getString(3);
            String employeeId = resultSet.getString(4);
            String customerId = resultSet.getString(5);

            String style = getHairCutStyle(resultSet.getString(6));

            String status = resultSet.getString(7);

            Appointment appointment = new Appointment(appoinmentId, time, date, employeeId, customerId,style,status);

            return appointment;
        }

        return null;
    }

    public static String getHairCutStyle(String id) throws SQLException {
        String sql = "SELECT hairCutStyle FROM hairCut WHERE hairCutId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            String hairStye = resultSet.getString(1);
            return hairStye;
        }
        return null;
    }





    public static List<String> getStatus() throws SQLException {
        String sql = "SELECT status FROM appointment";
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
        String sql = "SELECT status FROM appointment WHERE status = ?";

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


    public static boolean update(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointment SET time = ?, date = ?, employeeId = ?, customerId =?,  status = ? WHERE appointmentId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, appointment.getTime());
        pstm.setObject(2, appointment.getDate());
        pstm.setObject(3, appointment.getEmployeeId());
        pstm.setObject(4, appointment.getCustomerId());
        pstm.setObject(5, appointment.getStatus());
        pstm.setObject(6, appointment.getAppointmentId());

        return pstm.executeUpdate() > 0;
    }

}

