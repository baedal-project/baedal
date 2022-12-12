package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.domain.Store;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.jwt.TokenProvider;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    @Cacheable(value = "test")
    public ResponseDto<?> getAllStore(Pageable pageable) {

        return ResponseDto.success(storeRepository.findAll(pageable));
    }

    @Transactional
    @Cacheable(value = "test")
    public ResponseDto<?> getMemberStore( Long memberId, HttpServletRequest request) {

        //case1)case2) token validity check
        tokenProvider.tokenValidationCheck(request);

        //case3) member 존재하지 않을 때
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        //member의 address에서 '시'단위까지만 추출
        String[] address = member.getAddress().split(" ");

        //DB indexing 후 full text search 이용해서 해당 Store 반환
        List<Store> stores = storeRepository.findByAddressV2(address[0]);

        return ResponseDto.success(stores);
    }



}
