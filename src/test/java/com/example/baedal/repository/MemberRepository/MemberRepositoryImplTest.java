package com.example.baedal.repository.MemberRepository;

import com.example.baedal.domain.QMember;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.QMemberResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.example.baedal.domain.QMember.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void findDtoByQueryProjection(){
        List<MemberResponseDto> memberDtos = queryFactory
                .select(new QMemberResponseDto(member.memberId, member.name, member.address))
                .from(member)
                .fetch();

        for (MemberResponseDto memberDto : memberDtos){
            System.out.println("memberDto = " + memberDto);
        }
    }

}