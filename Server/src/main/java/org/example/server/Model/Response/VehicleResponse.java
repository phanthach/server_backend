package org.example.server.Model.Response;

public class VehicleResponse {
    private long vehicleId;
    private String plateNumber;
    private String vehicleType;
    private int seatCapacity;
    private String nameLayout;
    private String img;
    private int status;

    public VehicleResponse(long vehicleId, String plateNumber, String vehicleType, int seatCapacity, String nameLayout, String img, int status) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.seatCapacity = seatCapacity;
        this.nameLayout = nameLayout;
        this.img = img;
        this.status = status;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public String getNameLayout() {
        return nameLayout;
    }

    public void setNameLayout(String nameLayout) {
        this.nameLayout = nameLayout;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
