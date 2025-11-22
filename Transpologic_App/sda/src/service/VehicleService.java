package service;

import dao.VehicleDAO;
import dao.VehicleDAOImpl;
import model.Vehicle;

import java.util.List;

public class VehicleService {

    private final VehicleDAO dao = new VehicleDAOImpl();

    public boolean addVehicle(Vehicle v) {
        if (v.getRegistrationNumber().isEmpty() || v.getModel().isEmpty())
            return false;

        return dao.insertVehicle(v);
    }

    // Overloaded method for direct parameter input
    public boolean addVehicle(String regNo, String model, int capacity, String status) {
        Vehicle v = new Vehicle(regNo, model, capacity, status);
        return addVehicle(v);
    }

    public List<Vehicle> getAvailableVehicles() {
        return dao.getAvailableVehicles();
    }

    public boolean assignDriverToVehicle(String regNo, String driver) {
        return dao.assignDriver(regNo, driver);
    }

    public boolean updateShipmentStatus(String regNo, String status) {
        return dao.updateShipmentStatus(regNo, status);
    }
}