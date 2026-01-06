package org.example.server.Model.Repository;

import org.example.server.Model.Seat;
import org.example.server.Model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    @Query("SELECT s FROM Seat s WHERE s.layoutId =:layout_id")
    List<Seat> findByLayoutId(@Param("layout_id") int layout_id);

    void deleteSeatBySeatId(int seatId);
    void deleteByLayoutId(int layoutId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.layoutId = :layoutId")
    long countSeatsByLayoutId(@Param("layoutId") int layoutId);

    boolean existsByNameSeatAndLayoutId(String nameSeat, int layoutId);

    boolean existsByPositionXAndPositionYAndFloorAndLayoutId(
            int positionX, int positionY, int floor, int layoutId);

}
