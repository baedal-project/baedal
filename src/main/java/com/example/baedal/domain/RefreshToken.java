package com.example.baedal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private Member member;

  @Column(nullable = false)
  private String tokenValue;

  public RefreshToken(Member member) {
    this.member = member;
  }

  public void updateTokenValue(String refreshToken) {
    this.tokenValue = refreshToken;
  }

}
