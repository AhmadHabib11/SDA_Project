package service;

import dao.DriverDAO;
import dao.DriverDAOImpl;
import model.Driver;

import java.util.List;

public class DriverService {

    private final DriverDAO driverDAO = new DriverDAOImpl();

    public boolean addDriver(Driver d) {
        if (d.getName().isEmpty() || d.getCnic().isEmpty() || d.getLicenseNo().isEmpty())
            return false;

        if (d.getExperience() < 0 || d.getWorkingHours() < 0)
            return false;

        return driverDAO.insertDriver(d);
    }

    // Overloaded method for direct parameter input
    public boolean addDriver(String name, String cnic, String license, int experience, int hours) {
        Driver d = new Driver(0, name, cnic, license, experience, hours, null);
        return addDriver(d);
    }

    public List<Driver> getAllDrivers() {
        return driverDAO.getAllDrivers();
    }

    public boolean updateDriverVehicle(String name, String vehicle) {
        return driverDAO.updateDriverVehicle(name, vehicle);
    }
}