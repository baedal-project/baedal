package com.example.baedal.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllOrderResponseDto {

    private Long storeId;
    private String storeName;
    private String itemName;
    private Long itemId;
    private Integer amount;
    private int itemPrice;
    private Long memberId;
    private String memberName;
    private LocalDateTime createdAt;

    @QueryProjection
    public AllOrderResponseDto(Long storeId, String storeName, Long itemId, String itemName, Integer amount, int itemPrice, Long memberId, String memberName, LocalDateTime createdAt) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.amount = amount;
        this.itemPrice = itemPrice;
        this.memberId = memberId;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }


}
