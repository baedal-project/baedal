package com.example.baedal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//member라면 주문내역 확인
//store라면 주문서 확인
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCheckRequestDto {
    private Long storeId;
    private Long memberId;
}
