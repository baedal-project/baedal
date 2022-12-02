package com.example.baedal.repository;


import com.example.baedal.domain.Member;
import com.example.baedal.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
  Optional<RefreshToken> findByTokenValue(String refreshToken);

}
