package service;

import dao.ShipmentDAO;
import dao.ShipmentDAOImpl;
import database.DBConnection;
import util.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ShipmentAllocationService {

    private final ShipmentDAO shipmentDAO;

    public ShipmentAllocationService() {
        this.shipmentDAO = new ShipmentDAOImpl();
    }

    /**
     * Allocates shipment (sets vehicle_no in shipments table) and updates vehicle.shipment_status = 'Assigned'
     */
    public boolean allocateShipment(int shipmentId, String registrationNumber) {
        try {
            // 1) set vehicle_no in shipments
            boolean ok = shipmentDAO.allocateShipment(shipmentId, registrationNumber);
            if (!ok) return false;

            // 2) update vehicle record (transactionally would be better — simple two step here)
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pst = conn.prepareStatement(
                     "UPDATE vehicles SET shipment_status = 'Assigned' WHERE registration_number = ?")) {
                pst.setString(1, registrationNumber);
                pst.executeUpdate();
            }
            LoggerUtil.getLogger().info("Shipment " + shipmentId + " allocated to " + registrationNumber);
            return true;
        } catch (Exception ex) {
            LoggerUtil.getLogger().severe("allocation failed: " + ex.getMessage());
            return false;
        }
    }
}
