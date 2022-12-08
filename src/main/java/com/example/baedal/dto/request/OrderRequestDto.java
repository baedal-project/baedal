package com.example.baedal.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    //고유값(storeId)
    private Long storeId;
    //고유값(itemId)
    private List<Long> itemId;
    private List<Integer> amount;
    private Long memberId;
    //private String storeName;

}
