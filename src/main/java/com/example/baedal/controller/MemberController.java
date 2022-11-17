package com.example.baedal.controller;

import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    //멤버등록
    @PostMapping (value = "/api/member/signup")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }
    @GetMapping (value = "api/member")
    public ResponseDto<?> getAllMember() {
        return memberService.getAllMember();
    }
}
