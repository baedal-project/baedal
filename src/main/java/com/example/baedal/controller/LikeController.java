package com.example.baedal.controller;


import com.example.baedal.dto.request.LikeRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping(value = "/api/likes")
    public ResponseDto<?> postLike(@RequestBody LikeRequestDto requestDto) {
        return likeService.postLike(requestDto);
    }

}
