package controller;

import model.Shipment;
import model.Vehicle;
import service.ShipmentService;
import service.ShipmentServiceImpl;
import service.ShipmentAllocationService;
import service.VehicleService;
import util.LoggerUtil;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ShipmentController {
    private final ShipmentService service;
    private final ShipmentAllocationService allocationService;
    private final VehicleService vehicleService;

    public ShipmentController() {
        this.service = new ShipmentServiceImpl();
        this.allocationService = new ShipmentAllocationService();
        this.vehicleService = new VehicleService();
    }

    public boolean createShipment(String cargo, String weightText, String destination, String pickup) {
        try {
            int weight = Integer.parseInt(weightText.trim());
            Shipment s = new Shipment(cargo.trim(), weight, destination.trim(), pickup.trim());
            return service.createShipment(s);
        } catch (NumberFormatException nfe) {
            LoggerUtil.getLogger().warning("weight parse error: " + nfe.getMessage());
            throw new IllegalArgumentException("Weight must be a whole number");
        } catch (Exception ex) {
            LoggerUtil.getLogger().severe("createShipment failed: " + ex.getMessage());
            return false;
        }
    }

    public List<Shipment> getUnallocatedShipments() {
        try {
            return service.getUnallocatedShipments();
        } catch (Exception ex) {
            LoggerUtil.getLogger().severe("getUnallocatedShipments failed: " + ex.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public boolean allocateShipment(int shipmentId, String registrationNumber) {
        return allocationService.allocateShipment(shipmentId, registrationNumber);
    }

    // ==========================================
    // UI Helper - Load unallocated shipments
    // ==========================================
    public void loadUnallocatedShipments(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Shipment> list = getUnallocatedShipments();

        if (list.isEmpty()) {
            combo.addItem("No unallocated shipments available");
            return;
        }

        for (Shipment s : list) {
            combo.addItem("ID-" + s.getId() + " | " + s.getCargoType() +
                    " | " + s.getWeight() + "kg | To: " + s.getDestination());
        }
    }

    // ==========================================
    // UI Helper - Load available vehicles
    // ==========================================
    public void loadAvailableVehicles(DefaultTableModel model) {
        model.setRowCount(0);

        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();

        for (Vehicle v : vehicles) {
            model.addRow(new Object[]{
                    v.getRegistrationNumber(),
                    v.getModel(),
                    v.getCapacity(),
                    v.getShipmentStatus()
            });
        }
    }
}