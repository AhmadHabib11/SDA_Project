package service;

import model.Shipment;
import java.util.List;

public interface ShipmentService {
    boolean createShipment(Shipment s) throws Exception;
    Shipment getShipmentById(int id) throws Exception;
    List<Shipment> getUnallocatedShipments() throws Exception;
    boolean updateShipment(Shipment s) throws Exception;
    boolean deleteShipment(int id) throws Exception;
}
