//package bl;
//
//import database.DBConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AssignmentService {
//
//    // Fetch drivers from DB
//	// Fetch drivers from DB
//	public List<String> getAvailableDrivers() {
//	    List<String> drivers = new ArrayList<>();
//	    String sql = "SELECT name FROM drivers"; // no WHERE clause, fetch all drivers
//
//	    try (Connection conn = DBConnection.getConnection();
//	         PreparedStatement pst = conn.prepareStatement(sql);
//	         ResultSet rs = pst.executeQuery()) {
//
//	        while (rs.next()) {
//	            drivers.add(rs.getString("name"));
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return drivers;
//	}
//
//
//    // Fetch vehicles from DB
//    public List<String> getAvailableVehicles() {
//        List<String> vehicles = new ArrayList<>();
//        String sql = "SELECT registration_number, model, status FROM vehicles"; // fixed column names
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement pst = conn.prepareStatement(sql);
//             ResultSet rs = pst.executeQuery()) {
//
//            while (rs.next()) {
//                String regNo = rs.getString("registration_number");
//                String model = rs.getString("model");
//                String status = rs.getString("status");
//                vehicles.add(model + " - " + regNo + " (" + status + ")");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return vehicles;
//    }
//
//    // Assign driver to a vehicle
//    public boolean assignDriverToVehicle(String driver, String vehicleReg, String vehicleModel) {
//        String sqlVehicle = "UPDATE vehicles SET driver_name = ?, status = 'Assigned' WHERE registration_number = ?";
//        String sqlDriver  = "UPDATE drivers SET vehicle_assigned = ? WHERE name = ?";
//
//        try (Connection conn = DBConnection.getConnection()) {
//
//            // Update vehicle table
//            try (PreparedStatement pstVehicle = conn.prepareStatement(sqlVehicle)) {
//                pstVehicle.setString(1, driver);
//                pstVehicle.setString(2, vehicleReg);
//                pstVehicle.executeUpdate();
//            }
//
//            // Update driver table
//            try (PreparedStatement pstDriver = conn.prepareStatement(sqlDriver)) {
//                String vehicleInfo = vehicleReg + " (" + vehicleModel + ")";
//                pstDriver.setString(1, vehicleInfo);
//                pstDriver.setString(2, driver);
//                pstDriver.executeUpdate();
//            }
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//}
