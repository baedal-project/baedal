package com.example.baedal.controller;

import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    @PostMapping(value = "/api/members/{memberId}/login")
    public ResponseDto<?> login(@PathVariable Long memberId){
        System.out.println("memberId 잘 나오나 보자" + memberId);
        return memberService.login(memberId);
    }
    @GetMapping (value = "/api/members")
    public ResponseDto<?> getAllMember() {
        return memberService.getAllMember();
    }
    @GetMapping (value = "/api/members/{id}")
    public ResponseDto<?> getMember(@PathVariable Long id) {
        return memberService.getOneMember(id);
    }
}
