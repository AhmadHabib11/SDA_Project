package dao;

import model.Vehicle;
import java.util.List;

public interface VehicleDAO {

    boolean insertVehicle(Vehicle v);

    List<Vehicle> getAvailableVehicles();

    boolean assignDriver(String regNo, String driverName);

    boolean updateShipmentStatus(String regNo, String status);
}
