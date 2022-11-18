package com.example.baedal.service;

import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;


    @Transactional
    public ResponseDto<?> getAllStore() {

        return ResponseDto.success(storeRepository.findAll());
    }
}
