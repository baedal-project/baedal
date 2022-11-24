package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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
                        .Id(member.getMemberId())
                        .name(member.getName())
                        .address(member.getAddress())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    //114ms(4 members)
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllMember() {
        return ResponseDto.success(memberRepository.findIdNameAddress());
    }

    //149ms(4 members)
//    @Transactional(readOnly = true)
//    public ResponseDto<?> getAllMember() {
//        return ResponseDto.success(memberRepository.findAll());
//    }

    @Transactional
    public ResponseDto<?> getOneMember(Long id) {
        MemberResponseDto member =isPresentMember(id);
        if (null == member) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 유저 id 입니다.");
        }

        //before refactoring
//    return ResponseDto.success(
//            MemberResponseDto.builder()
//                    .Id(member.getMemberId())
//                    .name(member.getName())
//                    .address(member.getAddress())
//                    .modifiedAt(member.getModifiedAt())
//                    .createdAt(member.getCreatedAt())
//                    .build());

        //after refactoring(1개 조회라 그런지 시간 차이 많이 x)
        return ResponseDto.success(memberRepository.findByMemberIdCustom(id));

    }

    //before refactoring
//    @Transactional
//    public Member isPresentMember(Long id) {
//        Optional<Member> optionalMember = memberRepository.findByMemberId(id);
//        return optionalMember.orElse(null);
//    }

    //after refactoring
    @Transactional
    public MemberResponseDto isPresentMember(Long id) {
        Optional<MemberResponseDto> optionalMember = memberRepository.findByMemberIdCustom(id);
        return optionalMember.orElse(null);
    }

}
