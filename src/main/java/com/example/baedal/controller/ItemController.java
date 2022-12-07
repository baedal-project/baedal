package com.example.baedal.controller;


import com.example.baedal.dto.request.ItemRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "api/stores/{storeId}/items")
    public ResponseDto<?> getAllStore(@PathVariable Long storeId, HttpServletRequest request) {
        return itemService.getAllItems(storeId, request);
    }
}
