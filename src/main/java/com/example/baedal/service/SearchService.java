package com.example.baedal.service;

import com.example.baedal.domain.Member;
import com.example.baedal.domain.Store;
import com.example.baedal.dto.request.SearchRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /*problem:
    member 주소와 상점의 주소가 정확시 일치해야
    해당하는 store을 반환*/
//    @Transactional
//    public ResponseDto<?> search(SearchRequestDto requestDto) {
//        Member member = memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null);
//        if(null == member) {
//            return ResponseDto.fail("NOT_FOUND", "memberId is not exist");
//        }
//        List<Store> stores = storeRepository.findByNameContainsAndAddressContains(requestDto.getKeyword(), member.getAddress());
//        return ResponseDto.success(stores);
//    }

    /*
     * '시'단위까지 일치하는 가게들은 모두 반환
     * cons) table full scan 실행
     * v1) DB like 기능 사용*/
    @Transactional
    public ResponseDto<?> searchV1(SearchRequestDto requestDto, Pageable pageable) {
        //memberId로 member 찾기
        Member member = memberRepository.findByMemberId(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        //member의 address에서 '시'단위까지만 추출(주소가 시로 시작한다고 가정)
        String[] address = member.getAddress().split(" ");
        //System.out.println("주소 시까지 잘 나오나 보자" + address[0]);
        List<Store> stores = storeRepository.findByAddressV1(address[0],pageable);

        return ResponseDto.success(stores);
    }


    /*
     *'시'단위까지 일치하는 가게들은 모두 반환
     * 1.DB indexing 사용하여 생성하고 싶은 인덱스 컬럼을 지정하고
     * 2.생성 후 인덱스 조회 시, where 절이 포함된 쿼리를 조회
     * 3.인덱스로 저장된 key-value 값을 참조해서 결과 출력
     * pros) table full scan을 하지 않아도 되므로 성능이 좋음
     * v2) DB full text search 사용 */
    @Transactional
    public ResponseDto<?> searchV2(SearchRequestDto requestDto, Pageable pageable) {
        //memberId로 member 찾기
        Member member = memberRepository.findByMemberId(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        //member의 address에서 '시'단위까지만 추출
        String[] address = member.getAddress().split(" ");

        //DB indexing 후 full text search 이용해서 해당 Store 반환
        List<Store> stores = storeRepository.findByAddressV2(address[0],pageable);

        return ResponseDto.success(stores);
    }

}
