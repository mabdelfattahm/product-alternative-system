
package com.example.rating.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Alternative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String source;

    private double rating;

    private boolean approved = false; // Moderation flag

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "alternative", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}
