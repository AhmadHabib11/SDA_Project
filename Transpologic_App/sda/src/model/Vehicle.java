package model;

public class Vehicle {
    private String registrationNumber;
    private String model;
    private int capacity;
    private String status;
    private String shipmentStatus;
    private String driverName;

    // Constructor with all fields
    public Vehicle(String registrationNumber, String model, int capacity, 
                   String status, String shipmentStatus, String driverName) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.capacity = capacity;
        this.status = status;
        this.shipmentStatus = shipmentStatus;
        this.driverName = driverName;
    }

    // Constructor without driver name and shipment status (for new vehicles)
    public Vehicle(String registrationNumber, String model, int capacity, String status) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.capacity = capacity;
        this.status = status;
        this.shipmentStatus = "Available";
        this.driverName = null;
    }

    // Getters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public String getDriverName() {
        return driverName;
    }

    // Setters
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", model='" + model + '\'' +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                ", shipmentStatus='" + shipmentStatus + '\'' +
                ", driverName='" + driverName + '\'' +
                '}';
    }
}