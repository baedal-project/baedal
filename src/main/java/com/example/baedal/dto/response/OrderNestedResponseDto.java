package com.example.baedal.dto.response;

import com.example.baedal.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderNestedResponseDto {

    private Long ordersId;
    private Long memberId;
    private LocalDateTime orderDate;
    private String storeName;
    private List<OrderHasItemResponseDto> orderHasItems;

    public OrderNestedResponseDto(Orders orders){
        this.ordersId = orders.getOrdersId();
        this.memberId = orders.getMember().getMemberId();
        this.orderDate = orders.getCreatedAt();
        this.storeName = orders.getStoreName();
        this.orderHasItems = orders.getOrderHasItems().stream()
                .map(orderHasItem -> new OrderHasItemResponseDto(orderHasItem))
                .collect(Collectors.toList());
    }
}
