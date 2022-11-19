package com.example.baedal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllOrderResponseDto {

    private Long memberId;
    private Long storeId;
    private Long ordersId;
    private Long itemId;

}
