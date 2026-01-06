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

    @Column(name = "modId", length = 255)
    private int modId;

    @Column(name = "img", nullable = false, length = 255)
    private String img;

    @Column(name = "layout_id", nullable = false)
    private int layoutId;

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

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
