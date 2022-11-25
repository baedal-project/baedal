package com.example.baedal.repository.StoreRepository;

import com.example.baedal.domain.Store;
import com.example.baedal.dto.response.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryCustom {
    Page<StoreResponseDto> getAllStore(Pageable pageable);

//    List<Store> getOneStore(Long id);
}
