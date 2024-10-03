package org.example.server.Model.Repository;


import jakarta.transaction.Transactional;
import org.example.server.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.busCode = :busCode")
    Page<User> findByBusCode(@Param("busCode") String busCode, Pageable pageable);

    void deleteByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE User u SET u.isBlocked =:isBlocked WHERE u.phoneNumber =:phoneNumber")
    void updateIsBlocked(@Param("phoneNumber") String phoneNumber, @Param("isBlocked") int isBlocked);
}

