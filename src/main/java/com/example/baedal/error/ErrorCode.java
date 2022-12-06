package com.example.baedal.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //==========================sign up========================================
    SIGNUP_WRONG_NICKNAME(400, "닉네임이 존재하지 않습니다"),
    ALREADY_SAVED_NICKNAME(409,"중복된 닉네임입니다."),

    //=============================login=======================================
    MEMBER_NOT_FOUND(404,"사용자를 찾을 수 없습니다."),
    NICKNAME_EMPTY(400,"아이디를 입력해주세요"),
    PASSWORD_EMPTY(400,"비밀번호를 입력해주세요"),
    NICKNAME_MISMATCH(404,"아이디가 일치하지 않습니다"),
    PASSWORD_MISMATCH(404,"비밀번호가 일치하지 않습니다"),

    //================================token========================================
    INVALID_TOKEN(404,"Token이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(401, "토큰이 만료되었습니다. 다시 로그인 하세요."),

    //==============================500 INTERNAL SERVER ERROR========================

    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 고객센터에 문의해주세요"),
    BIND_Fails(500,"서버 에러입니다. 고객센터에 문의해주세요"),
    NOT_VALUE_AT(500,"서버 에러입니다. 고객센터에 문의해주세요"),
    NO_ELEMENT(500,"서버 에러입니다. 고객센터에 문의해주세요");
    //BIND_Fails(500,"서버 에러입니다. 고객센터에 문의해주세요");

    private final int status;
    private final String message;
}