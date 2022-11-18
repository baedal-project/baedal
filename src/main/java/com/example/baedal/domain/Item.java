package com.example.baedal.domain;


import com.example.baedal.shared.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderHasItem> orderHasItems = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="store_Id", nullable = true)
    private Store store;

}
