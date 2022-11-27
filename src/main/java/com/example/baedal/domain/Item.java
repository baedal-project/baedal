package com.example.baedal.domain;

import com.example.baedal.shared.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@BatchSize(size = 10)
public class Item extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
//    private int amount;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderHasItem> orderHasItems = new ArrayList<>();

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_Id", nullable = true)
    private Store store;

}
