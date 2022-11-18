package com.example.baedal.domain;

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
public class Orders extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="member_Id", nullable = false)
    private Member member;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "orders",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderHasItem> orderHasItems;


}
