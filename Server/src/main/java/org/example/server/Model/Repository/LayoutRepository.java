package org.example.server.Model.Repository;

import org.example.server.Model.Layout;
import org.example.server.Model.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
//    Lấy list layout phân trang bằng page
    @Query("SELECT l FROM Layout l WHERE l.modId =:modId")
    Page<Layout> findByModId(@Param("modId") int modId, Pageable pageable);
//Lấy list layout đang hoạt động
    @Query("SELECT l FROM Layout l WHERE l.modId =:modId AND l.status = 0")
    Page<Layout> findByModIdStatus(@Param("modId") int modId,Pageable pageable);

    void deleteByLayoutId(int layoutId);

    Layout findByLayoutId(int layoutId);

    @Modifying
    @Query("UPDATE Layout l SET l.status = :status WHERE l.layoutId = :layoutId")
    void updateStatusByLayoutId(@Param("layoutId") int layoutId, @Param("status") int status);

    @Modifying
    @Query("UPDATE Layout l SET l.nameLayout = :nameLayout, l.seatCapacity = :seatCapacity, l.x = :x, l.y = :y, l.status = :status WHERE l.layoutId = :layoutId")
    void updateLayout(@Param("nameLayout") String nameLayout,
                     @Param("seatCapacity") int seatCapacity,
                     @Param("x") int x,
                     @Param("y") int y,
                     @Param("status") int status,
                     @Param("layoutId") int layoutId);
}
