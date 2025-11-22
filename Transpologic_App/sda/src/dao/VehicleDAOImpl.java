package dao;

import database.DBConnection;
import model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public boolean insertVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles (registration_number, model, capacity, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, v.getRegistrationNumber());
            pst.setString(2, v.getModel());
            pst.setInt(3, v.getCapacity());
            pst.setString(4, v.getStatus());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE shipment_status='Available'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getString("registration_number"),
                        rs.getString("model"),
                        rs.getInt("capacity"),
                        rs.getString("status"),
                        rs.getString("shipment_status"),
                        rs.getString("driver_name")
                );
                list.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean assignDriver(String regNo, String driverName) {
        String sql = "UPDATE vehicles SET driver_name=?, status='Assigned' WHERE registration_number=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, driverName);
            pst.setString(2, regNo);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateShipmentStatus(String regNo, String status) {
        String sql = "UPDATE vehicles SET shipment_status=? WHERE registration_number=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, status);
            pst.setString(2, regNo);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
