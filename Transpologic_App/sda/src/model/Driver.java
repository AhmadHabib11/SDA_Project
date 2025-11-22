package model;

public class Driver {
    private int id;
    private String name;
    private String cnic;
    private String licenseNo;
    private int experience;
    private int workingHours;
    private String assignedVehicle;

    // Constructor
    public Driver(int id, String name, String cnic, String licenseNo, 
                  int experience, int workingHours, String assignedVehicle) {
        this.id = id;
        this.name = name;
        this.cnic = cnic;
        this.licenseNo = licenseNo;
        this.experience = experience;
        this.workingHours = workingHours;
        this.assignedVehicle = assignedVehicle;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCnic() {
        return cnic;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public int getExperience() {
        return experience;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public String getAssignedVehicle() {
        return assignedVehicle;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public void setAssignedVehicle(String assignedVehicle) {
        this.assignedVehicle = assignedVehicle;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cnic='" + cnic + '\'' +
                ", licenseNo='" + licenseNo + '\'' +
                ", experience=" + experience +
                ", workingHours=" + workingHours +
                ", assignedVehicle='" + assignedVehicle + '\'' +
                '}';
    }
}