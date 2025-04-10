package com.dathihida.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double discountPercentage;

    private LocalDate validityStartDate;

    private LocalDate validityEndDate;

    private double minimumOrderValue;

    private boolean isActive = true;

    @ManyToMany(mappedBy = "usedCoupons")
    @JsonIgnore
    private Set<User> usedByUsers = new HashSet<>();
}
