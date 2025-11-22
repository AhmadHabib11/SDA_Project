//package bl;
//
//import database.DBConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//
//public class DriverService {
//
//    // Add a new driver
//    public boolean addDriver(String name, String cnic, String license, int experience, int hours) {
//        String sql = "INSERT INTO drivers (name, cnic, license_no, experience, working_hours) " +
//                     "VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement pst = conn.prepareStatement(sql)) {
//
//            pst.setString(1, name);
//            pst.setString(2, cnic);
//            pst.setString(3, license);
//            pst.setInt(4, experience);
//            pst.setInt(5, hours);
//
//            int rows = pst.executeUpdate();
//            return rows > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // You can later add methods like getDriver(), updateDriver(), deleteDriver()
//}
