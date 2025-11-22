package dao;

import model.Shipment;
import java.util.List;

public interface ShipmentDAO {
    boolean create(Shipment s) throws Exception;
    Shipment getById(int id) throws Exception;
    List<Shipment> getUnallocated() throws Exception;
    boolean update(Shipment s) throws Exception;
    boolean delete(int id) throws Exception;
    boolean allocateShipment(int shipmentId, String vehicleNo) throws Exception; // sets vehicle_no
}
