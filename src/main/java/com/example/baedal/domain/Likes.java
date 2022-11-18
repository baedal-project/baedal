package com.example.baedal.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@JsonBackReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="store_Id", nullable = false)
    private Store store;

    //@JsonBackReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="member_Id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private double star;
}
