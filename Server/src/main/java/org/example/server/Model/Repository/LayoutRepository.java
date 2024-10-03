package org.example.server.Model.Repository;

import org.example.server.Model.Layout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    Page<Layout> findAll(Pageable pageable);
}
