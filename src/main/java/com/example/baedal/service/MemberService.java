package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
                    .Id(member.getId())
                    .name(member.getName())
                    .address(member.getAddress())
                    .modifiedAt(member.getModifiedAt())
                    .createdAt(member.getCreatedAt())
                    .build()
    );

    }
    @Transactional
    public Member isPresentMember(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElse(null);
    }

}
