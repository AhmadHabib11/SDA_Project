package service;

import dao.ShipmentDAO;
import dao.ShipmentDAOImpl;
import model.Shipment;
import util.ValidatorUtil;
import util.LoggerUtil;

import java.util.List;

public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentDAO dao;

    public ShipmentServiceImpl() {
        this.dao = new ShipmentDAOImpl();
    }

    @Override
    public boolean createShipment(Shipment s) throws Exception {
        // validation in BL
        if (!ValidatorUtil.isNonEmpty(s.getCargoType())) throw new IllegalArgumentException("Cargo type required");
        if (!ValidatorUtil.isPositive(s.getWeight())) throw new IllegalArgumentException("Weight must be > 0");
        if (!ValidatorUtil.isNonEmpty(s.getDestination())) throw new IllegalArgumentException("Destination required");
        // safe to call DAO
        return dao.create(s);
    }

    @Override
    public Shipment getShipmentById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid shipment id");
        return dao.getById(id);
    }

    @Override
    public List<Shipment> getUnallocatedShipments() throws Exception {
        return dao.getUnallocated();
    }

    @Override
    public boolean updateShipment(Shipment s) throws Exception {
        if (s.getId() <= 0) throw new IllegalArgumentException("Invalid id");
        return dao.update(s);
    }

    @Override
    public boolean deleteShipment(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid id");
        return dao.delete(id);
    }
}
