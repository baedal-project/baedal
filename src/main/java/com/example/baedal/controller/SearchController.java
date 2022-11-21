//package com.example.baedal.controller;
//
//import com.example.baedal.dto.request.SearchRequestDto;
//import com.example.baedal.dto.response.ResponseDto;
//import com.example.baedal.service.SearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class SearchController {
//
//    private final SearchService searchService;
//
//    @PostMapping(value = "/api/search")
//    public ResponseDto<?> postOrder(@RequestBody SearchRequestDto requestDto) {
//        return searchService.search(requestDto);
//    }
//}
