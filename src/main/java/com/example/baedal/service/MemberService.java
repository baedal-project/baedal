package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.dto.TokenDto;
import com.example.baedal.dto.request.LoginRequestDto;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.jwt.TokenProvider;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {

        //signup duplication check
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

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

        // unique nickname으로 validation
        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));

        if (null == member){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 유저입니다.");
        }

        //DB password check
        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_PASSWORD", "비밀번호가 올바르지 않습니다.");
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
        if (null == member) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 유저 id 입니다.");
        }

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
        Optional<Member> optionalMember = memberRepository.findByMemberId(id);
        return optionalMember.orElse(null);
    }
//    @Transactional
//    public MemberResponseDto isPresentMember(Long id) {
//        Optional<MemberResponseDto> optionalMember = memberRepository.findByMemberIdCustom(id);
//        return optionalMember.orElse(null);
//
//    }

    // 로그아웃
    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }
}
