package com.example.baedal.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor

public class StoreResponseDto {
    private Long storeId;
    private String name;
    private String address;
    private String category;
//    private List<ItemResponseDto> itemResponseDtoList;
    @QueryProjection
    public StoreResponseDto(Long storeId, String name, String address, String category){
        this.storeId = storeId;
        this.name = name;
        this.address = address;
        this.category = category;
    }

}
