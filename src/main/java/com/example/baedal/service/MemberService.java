package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.dto.TokenDto;
import com.example.baedal.dto.request.LoginRequestDto;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.CustomException;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.jwt.TokenProvider;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {

        //signup duplication check
        memberRepository.findByNickname(requestDto.getNickname()).ifPresent(member -> {
            throw new CustomException(ErrorCode.ALREADY_SAVED_NICKNAME);
        });

        //encode password
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = Member.builder()
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .password(encodedPassword)
                .address(requestDto.getAddress())
                .role(requestDto.getRole())
                .build();
        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .Id(member.getMemberId())
                        .name(member.getName())
                        .nickname(member.getNickname())
                        .address(member.getAddress())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {

        // case1) login 입력하지 않았을 경우 한번 더 걸러주기
        if (requestDto.getNickname() == null) {
            throw new CustomException(ErrorCode.NICKNAME_EMPTY);
        }

        // case2) password 입력하지 않았을 경우 한번 더 걸러주기
        if (requestDto.getPassword() == null) {
            throw new CustomException(ErrorCode.PASSWORD_EMPTY);
        }

        // case3) nickname에 해당하는 member 존재하는지 check
        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NICKNAME_MISMATCH.getMessage()));

        // case4) DB password check
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        //Access-Token, Refresh-Token 발급한 후 FE에 ServletResponse로 전달
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                //null
                MemberResponseDto.builder()
                        .Id(member.getMemberId())
                        .name(member.getName())
                        .nickname(member.getNickname())
                        .address(member.getAddress())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllMember() {
        //return ResponseDto.success(memberRepository.findAll());
        //refactoring
        return ResponseDto.success(memberRepository.findIdNameAddress());
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getOneMember(Long id) {
        Member member =isPresentMember(id);
        //MemberResponseDto member = isPresentMember(id);
//        if (null == member) {
//            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND.name(),
//                                    ErrorCode.MEMBER_NOT_FOUND.getMessage());
//        }

//        memberRepository.findByMemberId(id)
//                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        //before refactoring
//        return ResponseDto.success(
//                MemberResponseDto.builder()
//                        .Id(member.getMemberId())
//                        .name(member.getName())
//                        .nickname(member.getNickname())
//                        .address(member.getAddress())
//                        .createdAt(member.getCreatedAt())
//                        .modifiedAt(member.getModifiedAt())
//                        .build()
//        );
        //after refactoring
        return ResponseDto.success(memberRepository.findByMemberIdCustom(id));

    }

    @Transactional
    public Member isPresentMember(Long id) {
         return memberRepository.findByMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    }

    // 로그아웃
    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        Member member = tokenProvider.getMemberFromAuthentication();

        if (null == member) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return tokenProvider.deleteRefreshToken(member);
    }
}
