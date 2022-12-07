package com.example.baedal.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class ItemResponseDto {

    private Long itemId;
    private String name;
    private int price;
    private String category;

    @QueryProjection
    public ItemResponseDto(Long itemId, String name, int price, String category) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
