package org.example.server.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "layout")
public class Layout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layoutId", nullable = false)
    private int layoutId;

    @Column(name = "nameLayout", nullable = false, length = 255)
    private String nameLayout;

    @Column(name = "seatCapacity", nullable = false, length = 255)
    private int seatCapacity;

    @Column(name = "x", nullable = false, length = 255)
    private int x;

    @Column(name = "y", nullable = false, length = 255)
    private int y;

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
}
