package com.example.baedal.dto.response;

import com.example.baedal.domain.OrderHasItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHasItemResponseDto {

    private String itemName;    //상품명
    private int amount;  //주문 수량
    //주문 가격?

    public OrderHasItemResponseDto(OrderHasItem orderHasItem) {
        this.itemName = orderHasItem.getItem().getName();
        this.amount = orderHasItem.getAmount();
    }
}
