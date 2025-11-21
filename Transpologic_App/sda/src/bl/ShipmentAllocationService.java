package bl;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ShipmentAllocationService {

	public boolean allocateShipment(String shipmentId, String registrationNumber) {
	    String sql = "UPDATE shipments SET vehicle_no = ? WHERE id = ?";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pst = conn.prepareStatement(sql)) {

	        pst.setString(1, registrationNumber);
	        pst.setString(2, shipmentId);

	        int updated = pst.executeUpdate();

	        if (updated > 0) {
	            // Update vehicle shipment status
	            String vehicleUpdate = "UPDATE vehicles SET shipment_status = 'Assigned' WHERE registration_number = ?";
	            try (PreparedStatement pst2 = conn.prepareStatement(vehicleUpdate)) {
	                pst2.setString(1, registrationNumber);
	                pst2.executeUpdate();
	            }
	            return true;
	        } else {
	            return false;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}