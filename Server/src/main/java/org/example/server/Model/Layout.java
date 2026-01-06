package org.example.server.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "layout")
public class Layout implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layoutId", nullable = false)
    @JsonProperty("layoutId")
    private int layoutId;

    @Column(name = "nameLayout", nullable = false, length = 255)
    private String nameLayout;

    @Column(name = "seatCapacity", nullable = false, length = 255)
    private int seatCapacity;

    @Column(name = "x", nullable = false, length = 255)
    private int x;

    @Column(name = "y", nullable = false, length = 255)
    private int y;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "floor", nullable = false)
    private int floor;

    @Column(name="modId", nullable = false)
    private int modId;

    public Layout(int layoutId, String nameLayout, int seatCapacity, int x, int y, int status, int floor, int modId) {
        this.layoutId = layoutId;
        this.nameLayout = nameLayout;
        this.seatCapacity = seatCapacity;
        this.x = x;
        this.y = y;
        this.status = status;
        this.floor = floor;
        this.modId = modId;
    }

    public Layout() {

    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public String getNameLayout() {
        return nameLayout;
    }

    public void setNameLayout(String nameLayout) {
        this.nameLayout = nameLayout;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }
}
