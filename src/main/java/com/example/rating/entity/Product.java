
package com.example.rating.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean approved = false; // Moderation flag

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Alternative> alternatives = new ArrayList<>();
}
