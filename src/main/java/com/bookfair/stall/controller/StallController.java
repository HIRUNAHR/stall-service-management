package com.bookfair.stall.controller;

import com.bookfair.stall.dto.ApiResponse;
import com.bookfair.stall.model.Stall;
import com.bookfair.stall.enums.StallStatus;
import com.bookfair.stall.service.StallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stalls")
public class StallController {

    @Autowired
    private StallService stallService;

    // Secured Endpoint: Only Admins can create stalls
    @PostMapping
    public ResponseEntity<ApiResponse<Stall>> createStall(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody Stall stall) {
        
        // Role-Based Access Control
        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "Access Denied: Only Organizers/Admins can create stalls", null));
        }

        try {
            Stall createdStall = stallService.createStall(stall);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Stall created successfully", createdStall));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Error creating stall: " + e.getMessage(), null));
        }
    }

    // Public/User Endpoint: Anyone can view the map/stalls
    @GetMapping
    public ResponseEntity<ApiResponse<List<Stall>>> getAllStalls() {
        List<Stall> stalls = stallService.getAllStalls();
        return ResponseEntity.ok(new ApiResponse<>(true, "All stalls fetched successfully", stalls));
    }

    // Public/User Endpoint: View only available stalls
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Stall>>> getAvailableStalls() {
        List<Stall> stalls = stallService.getAvailableStalls();
        return ResponseEntity.ok(new ApiResponse<>(true, "Available stalls fetched successfully", stalls));
    }

    // View specific stall details
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Stall>> getStallById(@PathVariable Long id) {
        Optional<Stall> stall = stallService.getStallById(id);
        if (stall.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Stall fetched successfully", stall.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Stall not found", null));
        }
    }

    // Internal Endpoint: Used by Reservation Service to lock a stall
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Stall>> updateStallStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> statusUpdate) {
        try {
            StallStatus newStatus = StallStatus.valueOf(statusUpdate.get("status").toUpperCase());
            Stall updatedStall = stallService.updateStallStatus(id, newStatus);
            return ResponseEntity.ok(new ApiResponse<>(true, "Stall status updated successfully", updatedStall));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Invalid status provided", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}