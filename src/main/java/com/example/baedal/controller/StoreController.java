package com.example.baedal.controller;

import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;

    @GetMapping (value = "/api/stores")
    public ResponseDto<?> getAllStore(@PageableDefault(size = 10)Pageable pageable) {
        return storeService.getAllStore(pageable);
    }

//    @GetMapping (value = "/api/stores/{id}")
//    public ResponseDto<?> getOneStore(@PathVariable Long id) {
//        return storeService.getOneStore(id);
//    }
}
