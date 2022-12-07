package com.example.baedal.controller;

import com.example.baedal.dto.request.SearchRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /*problem:
    member 주소와 상점의 주소가 정확시 일치해야
    해당하는 store을 반환함*/
//    @PostMapping(value = "/api/search")
//    public ResponseDto<?> postOrder(@RequestBody SearchRequestDto requestDto) {
//        return searchService.search(requestDto);
//    }

    /*
    * '시'단위까지 일치하는 가게들은 모두 반환
    * v1) DB like 기능 사용*/
    @PostMapping(value = "/api/v1/search")
    public ResponseDto<?> postOrderV1(@RequestBody SearchRequestDto requestDto,
                                      @PageableDefault(page = 0, size = 10) Pageable pageable,
                                      HttpServletRequest request) {
        return searchService.searchV1(requestDto,pageable,request);
    }

    /*
     * '시'단위까지 일치하는 가게들은 모두 반환
     * v2) DB full-text search 기능 사용*/
    @PostMapping(value = "/api/v2/search")
    public ResponseDto<?> postOrderV2(@RequestBody SearchRequestDto requestDto,
                                      @PageableDefault(page = 0, size = 10) Pageable pageable,
                                      HttpServletRequest request) {
        return searchService.searchV2(requestDto,pageable,request);
    }


}
