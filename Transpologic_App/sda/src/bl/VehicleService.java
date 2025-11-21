package bl;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class VehicleService {

    // Add a new vehicle to DB
    public boolean addVehicle(String regNo, String model, int capacity, String status) {
        String sql = "INSERT INTO vehicles (registration_number, model, capacity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
             
            pst.setString(1, regNo);
            pst.setString(2, model);
            pst.setInt(3, capacity);
            pst.setString(4, status);

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // You can add more methods later: getVehicle(), updateVehicle(), deleteVehicle()
}
