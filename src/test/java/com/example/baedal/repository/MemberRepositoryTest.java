package com.example.baedal.repository;

import com.example.baedal.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
//@DisplayName("member 관련 test 시작")
class MemberRepositoryTest {

//    @PersistenceContext
//    EntityManager em;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @AfterEach
//    @DisplayName("auto_increment reset")
//    public void teardown() {
//        this.memberRepository.deleteAll();
//        this.em
//            .createNativeQuery("ALTER TABLE Member AUTO_INCREMENT=1")
////                    "SET @COUNT=0" +
////                    "UPDATE Member SET Member_Id = @COUNT\\:=@COUNT+1")
//            .executeUpdate();
//    }
//
//    @Test
//    @DisplayName("member 저장 잘 되어지나")
//    public void saveMember(){
//
//        //given
//        Member member = new Member();
//        member.setName("yeongmin");
//        member.setAddress("jecheon");
//        memberRepository.save(member);
//
//        //when
//        Member member1 = memberRepository.findByMemberId(1L).orElse(null);
//
//        //then
//        assertThat(member1.getName()).isEqualTo(member.getName());
//        assertThat(member1.getAddress()).isEqualTo(member.getAddress());
//
//    }

}