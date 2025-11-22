package model;

public class Shipment {
    private int id;
    private String cargoType;
    private int weight;
    private String destination;
    private String pickupPoint;
    private String vehicleNo; // null when unallocated

    public Shipment() {}

    public Shipment(int id, String cargoType, int weight, String destination, String pickupPoint, String vehicleNo) {
        this.id = id;
        this.cargoType = cargoType;
        this.weight = weight;
        this.destination = destination;
        this.pickupPoint = pickupPoint;
        this.vehicleNo = vehicleNo;
    }

    public Shipment(String cargoType, int weight, String destination, String pickupPoint) {
        this.cargoType = cargoType;
        this.weight = weight;
        this.destination = destination;
        this.pickupPoint = pickupPoint;
    }

    // getters / setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCargoType() { return cargoType; }
    public void setCargoType(String cargoType) { this.cargoType = cargoType; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getPickupPoint() { return pickupPoint; }
    public void setPickupPoint(String pickupPoint) { this.pickupPoint = pickupPoint; }
    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }
    
    @Override
    public String toString() {
        return "ID-" + id + " | " + cargoType + " | " + weight + "kg | To: " + destination;
    }
}
