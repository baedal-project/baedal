package com.example.baedal.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {

    private Long memberId;
    private Long storeId;

    //would be randomly allocated
    //private int star;

}
