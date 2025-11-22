package dao;

import model.Driver;
import java.util.List;

public interface DriverDAO {

    boolean insertDriver(Driver driver);

    Driver getDriverByName(String name);

    List<Driver> getAllDrivers();

    boolean updateDriverVehicle(String driverName, String vehicleInfo);

    boolean deleteDriver(int id);
}
