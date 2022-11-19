package com.example.baedal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHasItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderHasItemId;

    //FK
    //주문메뉴 테이블
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name="orders_Id", nullable = true)
    private Orders orders;

    //FK
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name="item_Id", nullable = true)
    private Item item;

    @Column(nullable = false)
    private int amount;
}
