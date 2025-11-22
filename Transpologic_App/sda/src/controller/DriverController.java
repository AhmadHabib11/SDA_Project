package controller;

import model.Driver;
import service.DriverService;

import java.util.List;

public class DriverController {

    private final DriverService service = new DriverService();

    public boolean addDriver(String name, String cnic, String license, int exp, int hours) {

        Driver d = new Driver(0, name, cnic, license, exp, hours, null);
        return service.addDriver(d);
    }

    public List<Driver> fetchDrivers() {
        return service.getAllDrivers();
    }

    public boolean assignVehicle(String driverName, String vehicleInfo) {
        return service.updateDriverVehicle(driverName, vehicleInfo);
    }
}
