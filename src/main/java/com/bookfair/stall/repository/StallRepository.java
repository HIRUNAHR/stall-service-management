package com.bookfair.stall.repository;

import com.bookfair.stall.model.Stall;
import com.bookfair.stall.enums.StallStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StallRepository extends JpaRepository<Stall, Long> {
    // Spring Data JPA automatically writes the SQL query for this!
    List<Stall> findByStatus(StallStatus status);
}