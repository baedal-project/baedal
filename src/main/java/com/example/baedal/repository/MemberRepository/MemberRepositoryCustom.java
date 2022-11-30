package com.example.baedal.repository.MemberRepository;

import com.example.baedal.dto.response.MemberResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<MemberResponseDto> findByMemberIdCustom(Long id);

    List<MemberResponseDto> findIdNameAddress();

    //Optional<Member> findByMemberId(Long id);
}
