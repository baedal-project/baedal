package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.dto.request.LoginRequestDto;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class) //가짜 객체 명시
class MemberServiceTest extends ServiceCustom {

    @Test
    @DisplayName("회원가입")
    public void 회원가입() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        ResponseDto<?> responseDto = memberService.createMember(memberRequestDto);

        //when
        MemberResponseDto memberResponseDto = (MemberResponseDto) responseDto.getData();

        //then
        assertThat(memberResponseDto.getName()).isEqualTo(memberRequestDto.getName());
        assertThat(memberResponseDto.getAddress()).isEqualTo(memberRequestDto.getAddress());
        assertThat(memberResponseDto.getNickname()).isEqualTo(memberRequestDto.getNickname());
    }

    @Test
    @DisplayName("회원가입시 중복 닉네임 예외")
    public void 회원가입시_중복된_닉네임_예외() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        memberService.createMember(memberRequestDto);

        //when
        MemberRequestDto test2 = MemberRequestDto.builder()
                .name("yeongmin")
                .nickname("yeongmin")
                .password("1234")
                .address("jecheon")
                .role("CONSUMER")
                .build();

        //then
        assertThatThrownBy(() -> memberService.createMember(test2))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("중복된 닉네임입니다.");

    }

    @Test
    @DisplayName("로그인")
    public void 로그인() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        memberService.createMember(memberRequestDto);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .nickname("yeongmin")
                .password("1234")
                .build();

        ResponseDto<?> responseDto = memberService.login(loginRequestDto, response);
        MemberResponseDto memberResponseDto = (MemberResponseDto) responseDto.getData();


        //then
        assertThat(memberResponseDto.getName()).isEqualTo(memberRequestDto.getName());
        assertThat(memberResponseDto.getAddress()).isEqualTo(memberRequestDto.getAddress());
        assertThat(memberResponseDto.getNickname()).isEqualTo(memberRequestDto.getNickname());
    }

    @Test
    @DisplayName("로그인시 닉네임 없을 때 예외")
    public void 로그인시_닉네임_빈값일때_예외() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        memberService.createMember(memberRequestDto);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .nickname(null)
                .password("1234")
                .build();

        //then
        assertThatThrownBy(() -> memberService.login(loginRequestDto, response))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("아이디를 입력해주세요");

    }

    @Test
    @DisplayName("로그인시 비밀번호 없을 때 예외")
    public void 로그인시_비밀번호_빈값일때_예외() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        memberService.createMember(memberRequestDto);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .nickname("yeongmin")
                .password(null)
                .build();

        //then
        assertThatThrownBy(() -> memberService.login(loginRequestDto, response))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("비밀번호를 입력해주세요");
    }

    @Test
    @DisplayName("로그인시 닉네임 잘못 입력했을 때 예외")
    public void 로그인시_닉네임_잘못_입력했을_때_예외() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        memberService.createMember(memberRequestDto);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .nickname("yeongmin12")
                .password("1234")
                .build();

        //then
        assertThatThrownBy(() -> memberService.login(loginRequestDto, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디가 일치하지 않습니다");

    }

    @Test
    @DisplayName("로그인시 비밀번호 잘못 입력했을 때 예외")
    public void 로그인시_비밀번호_잘못_입력했을_때_예외() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        memberService.createMember(memberRequestDto);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .nickname("yeongmin")
                .password("12345")
                .build();

        //then
        assertThatThrownBy(() -> memberService.login(loginRequestDto, response))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다");

    }

    @Test
    @DisplayName("모든 멤버 조회")
    public void 모든_멤버_조회() {
        //given
        int memberNum = 30;
        List<MemberRequestDto> memberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            memberList.add(MemberRequestDto.builder()
                    .nickname("nickname" + ++memberNum)
                    .name("name" + ++memberNum)
                    .role("CONSUMER")
                    .address("jecheon" + ++memberNum)
                    .password("pwd" + ++memberNum)
                    .build());
        }

        Iterator<MemberRequestDto> iterator = memberList.iterator();

        while (iterator.hasNext()) {
            memberService.createMember(iterator.next());
        }

        List<Member> findAll = memberRepository.findAll();

        Assertions.assertThat(findAll.size()).isEqualTo(10);
        Assertions.assertThat(findAll.size()).isNotEqualTo(100);
    }

    @Test
    @DisplayName("단일 멤버 조회")
    public void 단일_멤버_조회() {
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("yeongmin", "yeongmin", "1234", "jecheon", "CONSUMER");
        ResponseDto<?> member1 = memberService.createMember(memberRequestDto);

        //when
        ResponseDto<?> member = memberService.getOneMember(((MemberResponseDto) member1.getData()).getId());
        Optional<MemberResponseDto> memberResponseDto = (Optional<MemberResponseDto>) member.getData();
        //then
        assertThat(memberResponseDto.get().getNickname()).isEqualTo("yeongmin");
    }
}