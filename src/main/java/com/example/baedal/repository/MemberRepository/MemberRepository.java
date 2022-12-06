package com.example.baedal.repository.MemberRepository;

import com.example.baedal.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByMemberId(Long id);

    boolean existsByNickname(String nickname);

    Optional<Member> findByNickname(String nickname);
}
