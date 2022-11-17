package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        Member member = Member.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .build();
        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .Id(member.getId())
                        .name(member.getName())
                        .address(member.getAddress())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }
}
