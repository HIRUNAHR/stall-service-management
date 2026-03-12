package com.bookfair.stall.service;

import com.bookfair.stall.model.Stall;
import com.bookfair.stall.enums.StallStatus;
import com.bookfair.stall.repository.StallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StallService {

    @Autowired
    private StallRepository stallRepository;

    public Stall createStall(Stall stall) {
        // Default to AVAILABLE if the admin doesn't specify a status
        if (stall.getStatus() == null) {
            stall.setStatus(StallStatus.AVAILABLE);
        }
        return stallRepository.save(stall);
    }

    public List<Stall> getAllStalls() {
        return stallRepository.findAll();
    }

    public List<Stall> getAvailableStalls() {
        return stallRepository.findByStatus(StallStatus.AVAILABLE);
    }

    public Optional<Stall> getStallById(Long id) {
        return stallRepository.findById(id);
    }

    public Stall updateStallStatus(Long id, StallStatus newStatus) {
        Optional<Stall> optionalStall = stallRepository.findById(id);
        if (optionalStall.isPresent()) {
            Stall stall = optionalStall.get();
            stall.setStatus(newStatus);
            return stallRepository.save(stall);
        }
        throw new RuntimeException("Stall not found with id: " + id);
    }
}