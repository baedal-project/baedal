package com.example.baedal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Store extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
    private String category;

    //평균 평점
    @Column(nullable = false)
    private double avgStar = 0.00;

    //@JsonManagedReference
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Item> items = new ArrayList<>();

    //@JsonManagedReference
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Likes> likes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Orders> orders = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_Id", nullable = false)
    private Member member;

}
