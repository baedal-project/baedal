package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.domain.Store;
import com.example.baedal.dto.request.SearchRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> search(SearchRequestDto requestDto) {
        Member member = memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null);
        List<Store> stores = storeRepository.findByNameContainsAndAddressContains(requestDto.getKeyword(), member.getAddress());

        return ResponseDto.success(stores);
    }
}
