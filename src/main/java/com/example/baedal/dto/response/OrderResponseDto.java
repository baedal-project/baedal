package com.example.baedal.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long storeId;
    //고유값(itemId)
    private List<Long> itemId;
    private List<Integer> amount;
    private Long memberId;
    private LocalDateTime createdAt;
}
