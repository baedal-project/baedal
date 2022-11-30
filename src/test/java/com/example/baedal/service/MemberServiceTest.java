package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.shared.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("대소문자 구분")
    public void testCapital(){
        Member member = memberRepository.findByMemberId(1L).orElse(null);

    }

}