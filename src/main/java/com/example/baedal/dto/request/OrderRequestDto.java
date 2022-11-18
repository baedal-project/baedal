package com.example.baedal.dto.request;


import com.example.baedal.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    //고유값(storeId)
    private Long storeId;
    //고유값(itemId)
    private List<Long> itemId;
    private Long memberId;

}
