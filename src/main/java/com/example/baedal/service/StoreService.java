package com.example.baedal.service;

import com.example.baedal.domain.Store;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@EnableCaching
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;


    @Transactional
    @Cacheable(value = "test")
    public ResponseDto<?> getAllStore(Pageable pageable) {

        return ResponseDto.success(storeRepository.findAll(pageable));
    }
    
}
