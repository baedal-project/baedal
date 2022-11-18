package com.example.baedal.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long Id;

    //FK
    //주문메뉴 테이블
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name="orders_Id", nullable = true)
    private Orders orders;

    //FK
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name="item_Id", nullable = true)
    private Item item;

    public OrderHasItem(Item item){
        this.item = item;
    }
}
