package service;

import dao.DriverDAO;
import dao.DriverDAOImpl;
import dao.VehicleDAO;
import dao.VehicleDAOImpl;
import model.Driver;
import model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class AssignmentService {

    private final DriverDAO driverDAO = new DriverDAOImpl();
    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    /**
     * Get list of available drivers (not assigned to any vehicle)
     */
    public List<String> getAvailableDrivers() {
        List<String> driverNames = new ArrayList<>();
        List<Driver> drivers = driverDAO.getAllDrivers();

        for (Driver d : drivers) {
            // Only include drivers without assigned vehicles
            if (d.getAssignedVehicle() == null || d.getAssignedVehicle().isEmpty()) {
                driverNames.add(d.getName());
            }
        }

        return driverNames;
    }

    /**
     * Get list of available vehicles with formatted display strings
     */
    public List<String> getAvailableVehicles() {
        List<String> vehicleStrings = new ArrayList<>();
        List<Vehicle> vehicles = vehicleDAO.getAvailableVehicles();

        for (Vehicle v : vehicles) {
            String display = v.getModel() + " - " + v.getRegistrationNumber() + 
                           " (" + v.getCapacity() + "kg) - " + v.getStatus();
            vehicleStrings.add(display);
        }

        return vehicleStrings;
    }

    /**
     * Assign a driver to a vehicle
     */
    public boolean assignDriverToVehicle(String driverName, String regNo, String vehicleModel) {
        try {
            // Update vehicle with driver assignment
            boolean vehicleUpdated = vehicleDAO.assignDriver(regNo, driverName);
            
            // Update driver with vehicle assignment
            boolean driverUpdated = driverDAO.updateDriverVehicle(driverName, vehicleModel);

            return vehicleUpdated && driverUpdated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}