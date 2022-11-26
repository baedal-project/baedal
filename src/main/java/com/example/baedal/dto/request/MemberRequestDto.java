package com.example.baedal.dto.request;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private String name;
    private String address;

    @QueryProjection
    public MemberRequestDto(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
