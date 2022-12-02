package com.example.baedal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    private String nickname;
    private String password;
    //store 주인일 경우 masterKey 넘겨야 함
    private String role;

}
