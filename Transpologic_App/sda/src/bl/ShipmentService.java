package bl;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ShipmentService {

    public boolean createShipment(String cargo, String weight, String destination, String pickup) {
        String sql = "INSERT INTO shipments (cargo_type, weight, destination, pickup_point) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, cargo);
            pst.setString(2, weight);
            pst.setString(3, destination);
            pst.setString(4, pickup);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
