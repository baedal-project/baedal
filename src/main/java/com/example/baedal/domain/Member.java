package com.example.baedal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
//    private String address;
    @Embedded
    @AttributeOverride(name = "address", column = @Column(name = "home_address"))
    @AttributeOverride(name = "detail", column = @Column(name = "home_address_detail"))
    private Address homeAddress;

    @Embedded
    @AttributeOverride(name = "address", column = @Column(name = "company_address"))
    @AttributeOverride(name = "detail", column = @Column(name = "company_address_detail"))
    private Address companyAddress;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Orders> orders = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Likes> likes = new ArrayList<>();
}
