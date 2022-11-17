package com.example.baedal.controller;

import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;

    @GetMapping (value = "api/store")
    public ResponseDto<?> getAllStore() {
        return storeService.getAllStore();
    }
}
