package com.example.baedal.controller;

import com.example.baedal.dto.request.LoginRequestDto;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    //멤버등록
    @PostMapping (value = "/api/members/signup")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    //로그인
    @PostMapping(value = "/api/members/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
                                HttpServletResponse response){
        return memberService.login(requestDto, response);
    }

    //멤버 전체 조회
    @GetMapping (value = "/api/members")
    public ResponseDto<?> getAllMember() {
        return memberService.getAllMember();
    }

    //멤버 상세 조회
    @GetMapping (value = "/api/members/{id}")
    public ResponseDto<?> getMember(@PathVariable Long id) {
        return memberService.getOneMember(id);
    }

    // 로그아웃
    @PostMapping("/api/members/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }
}
