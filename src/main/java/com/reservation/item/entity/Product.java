package com.reservation.item.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity(name = "Product")
@Table(name = "_product",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_unique", columnNames = "name")
        })
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    @Column(name = "added_date", nullable = false, updatable = false)
    private Date addedDate = new Date();
}
