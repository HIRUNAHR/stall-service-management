package com.bookfair.stall.model;

import com.bookfair.stall.enums.StallSize;
import com.bookfair.stall.enums.StallStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stalls")
@Data // Lombok: auto-generates getters, setters, toString
@NoArgsConstructor
@AllArgsConstructor
public class Stall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String stallCode; // e.g., A-01, B-12

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StallSize size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StallStatus status;

    private String section; // Optional: To help group them on the venue map
}