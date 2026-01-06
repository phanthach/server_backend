package org.example.server.Service;

import jakarta.transaction.Transactional;
import org.example.server.Model.Repository.SeatRepository;
import org.example.server.Model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;
    public List<Seat> getAllSeats(int layoutId){
        return seatRepository.findByLayoutId(layoutId);
    }

    public void addSeat(Seat seat){
        seatRepository.save(seat);
    }
    @Transactional
    public void deleteSeat(int seatId) {
        seatRepository.deleteSeatBySeatId(seatId);
    }
    @Transactional
    public void deleteSeatsByLayoutId(int layoutId) {
        seatRepository.deleteByLayoutId(layoutId);  // Xóa tất cả ghế theo layoutId
    }

    public long countSeatsByLayoutId(int layoutId) {
        return seatRepository.countSeatsByLayoutId(layoutId);
    }
    public boolean existsByNameSeatAndLayoutId(
            String nameSeat,
            int layoutId) {
        return seatRepository.existsByNameSeatAndLayoutId(
                nameSeat,
                layoutId);
    }
    public boolean existsByPositionXAndPositionYAndFloorAndLayoutId(
            int positionX,
            int positionY,
            int floor,
            int layoutId) {
        return seatRepository.existsByPositionXAndPositionYAndFloorAndLayoutId(
                positionX,
                positionY,
                floor,
                layoutId);
    }
}
