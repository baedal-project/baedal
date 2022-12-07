package com.example.baedal.jwt;

import com.example.baedal.domain.Member;
import com.example.baedal.domain.RefreshToken;
import com.example.baedal.dto.TokenDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.CustomException;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.jwt.user.UserDetailsImpl;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class TokenProvider {

  private static final String BEARER_TYPE = "Bearer ";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;    // 30분
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

  private final Key key;

  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  public TokenProvider(
          @Value("${jwt.secret}") String secretKey,
          MemberRepository memberRepository,
          RefreshTokenRepository refreshTokenRepository
  ) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    this.memberRepository = memberRepository;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public TokenDto generateTokenDto(Member member) {

    long now = (new Date().getTime());

    // 액세스 토큰 생성
    String accessToken = BEARER_TYPE + Jwts.builder()
            // 아이디, 닉네임, 만료시간 토큰에 담기
            .setId(member.getMemberId().toString())
            .setSubject(member.getNickname())
            .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    // 리프레시 토큰 생성
    String refreshToken = Jwts.builder()
            // 만료시간 토큰에 담기
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    // 기존에 발급된 리프레시 토큰이 존재한다면 가져오고 없으면 새로 만들기
    RefreshToken refreshTokenObject = refreshTokenRepository.findByMember(member)
            .orElse(new RefreshToken(member));

    // 토큰 설정하고 데이터베이스에 저장
    refreshTokenObject.updateTokenValue(refreshToken);
    refreshTokenRepository.save(refreshTokenObject);

    return new TokenDto(accessToken, refreshToken);
  }

  public Authentication getAuthentication(String accessToken) {
    // 토큰 payload 읽어오기
    Claims claims = parseClaims(accessToken);
    // 토큰의 memberId 추출
    Long memberId = Long.parseLong(claims.getId());
    // Member 객체 가져오기
    Member member = memberRepository.findById(memberId).orElseThrow();
    // UserDetails 로 변환
    UserDetailsImpl principal = new UserDetailsImpl(member);
    // Authentication 객체 생성
    return new UsernamePasswordAuthenticationToken(principal, "", null);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException expiredJwtException) {
      log.info("Expired JWT token, 만료된 JWT token 입니다.");
      // 토큰 만료 익셉션
      throw expiredJwtException;
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .getBody();
  }

  public Member getMemberFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || AnonymousAuthenticationToken.class.
            isAssignableFrom(authentication.getClass())) {
      return null;
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
  }

  @Transactional(readOnly = true)
  public RefreshToken isPresentRefreshToken(Member member) {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
    return optionalRefreshToken.orElse(null);
  }

  @Transactional
  public ResponseDto<?> deleteRefreshToken(Member member) {
    RefreshToken refreshToken = isPresentRefreshToken(member);

    if (null == refreshToken) {
      throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    refreshTokenRepository.delete(refreshToken);
    return ResponseDto.success("로그아웃 완료");
  }

  /*token validation check*/
  public void tokenValidationCheck(HttpServletRequest request) {
    //case1) Refresh-Token이 Null일 때
    if (null == request.getHeader("Refresh-Token")) {
      throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    //case2) Authorization이 Null일 때
    if (null == request.getHeader("Authorization")) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }
  }
}
