package com.example.baedal;

import com.example.baedal.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.example.baedal.domain.QMember.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("query initializing test")
class BaedalApplicationTests {

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void before() {
        Member member1 = new Member("member1","jecheon");
        Member member2 = new Member("member2","seoul");

        em.persist(member1);
        em.persist(member2);

    }

    @Test
    @DisplayName("querydsl 잘 되나 확인")
    public void startQuerydsl() throws Exception {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.name.eq("member1"),
                        member.address.eq("jecheon"))
                .fetchOne();

        assertThat(findMember.getName()).isEqualTo("member1");
        assertThat(findMember.getAddress()).isEqualTo("jecheon");

     }

}
