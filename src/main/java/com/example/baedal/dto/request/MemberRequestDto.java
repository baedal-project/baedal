package com.example.baedal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    //actual name
    private String name;
    //should be unique nickname
    private String nickname;
    private String password;
    private String address;
    private String role;

//    @QueryProjection
//    public MemberRequestDto(String name, String address) {
//        this.name = name;
//        this.address = address;
//    }
}
