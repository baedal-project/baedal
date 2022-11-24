package com.example.baedal.repository.MemberRepository;


import com.example.baedal.domain.Member;
import com.example.baedal.dto.request.QMemberRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.QMemberResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.baedal.domain.QMember.*;

//queryDsl 구현부
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MemberResponseDto> findByMemberIdCustom(Long id){

        return Optional.ofNullable(queryFactory
                .select(new QMemberResponseDto(member.memberId,member.name,member.address))
                .from(member)
                .where(member.memberId.eq(id))
                .fetchOne());

    }

    @Override
    public List<MemberResponseDto> findIdNameAddress() {
        return queryFactory
                .select(new QMemberResponseDto(member.memberId,member.name,member.address))
                .from(member)
                .fetch();
    }


}
