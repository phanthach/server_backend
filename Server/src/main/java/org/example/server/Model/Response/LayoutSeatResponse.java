package org.example.server.Model.Response;

import org.example.server.Model.Seat;

import java.util.List;

public class LayoutSeatResponse {
//    val message: String,
//    val status: Int,
//    val layoutId: Int?,
//    val seatCapacity: Int?,
//    val x: Int?,
//    val y: Int?,
//    val floor: Int?,
//    val listSeat: List<Seat>? = null
    private String message;
    private Integer status;
    private Integer layoutId;
    private Integer seatCapacity;
    private Integer x;
    private Integer y;
    private Integer floor;
    private List<Seat> listSeat;

    public LayoutSeatResponse() {
    }

    public LayoutSeatResponse(String message, Integer status, Integer layoutId, Integer seatCapacity, Integer x, Integer y, Integer floor, List<Seat> listSeat) {
        this.message = message;
        this.status = status;
        this.layoutId = layoutId;
        this.seatCapacity = seatCapacity;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.listSeat = listSeat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Integer layoutId) {
        this.layoutId = layoutId;
    }

    public Integer getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public List<Seat> getListSeat() {
        return listSeat;
    }

    public void setListSeat(List<Seat> listSeat) {
        this.listSeat = listSeat;
    }
}
