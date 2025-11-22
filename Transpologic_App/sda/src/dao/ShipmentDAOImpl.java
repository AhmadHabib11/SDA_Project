package dao;

import database.DBConnection;
import model.Shipment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.LoggerUtil;

public class ShipmentDAOImpl implements ShipmentDAO {

    @Override
    public boolean create(Shipment s) throws Exception {
        String sql = "INSERT INTO shipments (cargo_type, weight, destination, pickup_point) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, s.getCargoType());
            pst.setInt(2, s.getWeight());
            pst.setString(3, s.getDestination());
            pst.setString(4, s.getPickupPoint());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) s.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException ex) {
            LoggerUtil.getLogger().severe("create shipment sql error: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Shipment getById(int id) throws Exception {
        String sql = "SELECT id, cargo_type, weight, destination, pickup_point, vehicle_no FROM shipments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Shipment(
                        rs.getInt("id"),
                        rs.getString("cargo_type"),
                        rs.getInt("weight"),
                        rs.getString("destination"),
                        rs.getString("pickup_point"),
                        rs.getString("vehicle_no")
                    );
                }
            }
            return null;
        } catch (SQLException ex) {
            LoggerUtil.getLogger().severe("getById error: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Shipment> getUnallocated() throws Exception {
        String sql = "SELECT id, cargo_type, weight, destination, pickup_point FROM shipments WHERE vehicle_no IS NULL";
        List<Shipment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Shipment(
                    rs.getInt("id"),
                    rs.getString("cargo_type"),
                    rs.getInt("weight"),
                    rs.getString("destination"),
                    rs.getString("pickup_point"),
                    null
                ));
            }
            return list;
        } catch (SQLException ex) {
            LoggerUtil.getLogger().severe("getUnallocated error: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean update(Shipment s) throws Exception {
        String sql = "UPDATE shipments SET cargo_type = ?, weight = ?, destination = ?, pickup_point = ?, vehicle_no = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, s.getCargoType());
            pst.setInt(2, s.getWeight());
            pst.setString(3, s.getDestination());
            pst.setString(4, s.getPickupPoint());
            pst.setString(5, s.getVehicleNo());
            pst.setInt(6, s.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerUtil.getLogger().severe("update error: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM shipments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerUtil.getLogger().severe("delete error: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean allocateShipment(int shipmentId, String vehicleNo) throws Exception {
        String sql = "UPDATE shipments SET vehicle_no = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, vehicleNo);
            pst.setInt(2, shipmentId);
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerUtil.getLogger().severe("allocateShipment error: " + ex.getMessage());
            throw ex;
        }
    }
}
