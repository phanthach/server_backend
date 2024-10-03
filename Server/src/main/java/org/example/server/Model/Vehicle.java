package org.example.server.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicleId", nullable = false)
    private long vehicleId;

    @Column(name = "plateNumber", nullable = false, length = 255)
    private String plateNumber;

    @Column(name = "vehicleType", nullable = false, length = 255)
    private String vehicleType;

    @Column(name = "seatCapacity", nullable = false, length = 255)
    private String seatCapacity;

    @Column(name = "modId", length = 255)
    private int modId;

    @Column(name = "x", nullable = false, length = 255)
    private int x;

    @Column(name = "y", nullable = false, length = 255)
    private int y;

    @Column(name = "img", nullable = false, length = 255)
    private String img;

    @Column(name = "status", nullable = false, length = 255)
    private int status;

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

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
