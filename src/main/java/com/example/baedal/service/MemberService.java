package com.example.baedal.service;

import com.example.baedal.domain.Address;
import com.example.baedal.domain.Member;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        Member member = Member.builder()
                .name(requestDto.getName())
                .homeAddress(new Address(requestDto.getHomeAddress(),requestDto.getHomeDetail()))
                .companyAddress(new Address(requestDto.getCompanyAddress(),requestDto.getCompanyDetail()))
                .build();
        memberRepository.save(member);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .Id(member.getMemberId())
                        .name(member.getName())
                        .homeAddress(member.getHomeAddress())
                        .companyAddress(member.getCompanyAddress())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }
    @Transactional
    public ResponseDto<?> getAllMember() {
        return ResponseDto.success(memberRepository.findAll());
    }

    @Transactional
    public ResponseDto<?> getOneMember(Long id) {
        Member member =isPresentMember(id);
        if (null == member) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 유저 id 입니다.");
        }
    return ResponseDto.success(
            MemberResponseDto.builder()
                    .Id(member.getMemberId())
                    .name(member.getName())
                    .homeAddress(member.getHomeAddress())
                    .companyAddress(member.getCompanyAddress())
                    .modifiedAt(member.getModifiedAt())
                    .createdAt(member.getCreatedAt())
                    .build()
    );

    }
    @Transactional
    public Member isPresentMember(Long id) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(id);
        return optionalMember.orElse(null);
    }

}
