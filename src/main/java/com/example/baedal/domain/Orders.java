package com.example.baedal.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    @Column(nullable = false)
    private String storeName;

    //@JsonBackReference
    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_Id", nullable = false)
    private Member member;

    @JsonIgnore
    //@BatchSize(size = 10)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "orders",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderHasItem> orderHasItems = new ArrayList<>();


}
