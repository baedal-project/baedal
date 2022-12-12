package com.example.baedal.domain;

import com.example.baedal.error.CustomException;
import com.example.baedal.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int price;

    //@Enumerated(EnumType.STRING)
    private String category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderHasItem> orderHasItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id", nullable = true)
    private Store store;

    public Integer changeStock(int amount){
        //case1) this.amount < amount
        if (this.amount < amount){
            throw new CustomException(ErrorCode.NOT_ENOUGH_STOCK);
        } else if (this.amount == 0){
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        } else{
            this.amount -= amount;  //stock 감소
        }
        return this.amount;
    }

}
