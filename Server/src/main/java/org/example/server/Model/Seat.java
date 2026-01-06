package org.example.server.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name ="seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatId", nullable = false)
    private int seatId;

    @Column(name = "layoutId", nullable = false)
    private int layoutId;

    @Column(name = "nameSeat", nullable = false)
    private String nameSeat;

    @Column(name = "positionX", nullable = false)
    private int positionX;

    @Column(name = "positionY", nullable = false)
    private int positionY;

    @Column(name = "floor", nullable = false)
    private int floor;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public String getNameSeat() {
        return nameSeat;
    }

    public void setNameSeat(String nameSeat) {
        this.nameSeat = nameSeat;
    }

    public int getPosition_x() {
        return positionX;
    }

    public void setPosition_x(int positionX) {
        this.positionX = positionX;
    }

    public int getPosition_y() {
        return positionY;
    }

    public void setPosition_y(int positionY) {
        this.positionY = positionY;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
