package dao;

import database.DBConnection;
import model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements DriverDAO {

    @Override
    public boolean insertDriver(Driver d) {
        String sql = "INSERT INTO drivers (name, cnic, license_no, experience, working_hours) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, d.getName());
            pst.setString(2, d.getCnic());
            pst.setString(3, d.getLicenseNo());
            pst.setInt(4, d.getExperience());
            pst.setInt(5, d.getWorkingHours());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Driver getDriverByName(String name) {
        String sql = "SELECT * FROM drivers WHERE name = ?";
        Driver d = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                d = new Driver(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cnic"),
                        rs.getString("license_no"),
                        rs.getInt("experience"),
                        rs.getInt("working_hours"),
                        rs.getString("vehicle_assigned")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }


    @Override
    public List<Driver> getAllDrivers() {
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT * FROM drivers";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Driver d = new Driver(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cnic"),
                        rs.getString("license_no"),
                        rs.getInt("experience"),
                        rs.getInt("working_hours"),
                        rs.getString("vehicle_assigned")
                );
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public boolean updateDriverVehicle(String driverName, String vehicleInfo) {
        String sql = "UPDATE drivers SET vehicle_assigned = ? WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, vehicleInfo);
            pst.setString(2, driverName);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteDriver(int id) {
        String sql = "DELETE FROM drivers WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
