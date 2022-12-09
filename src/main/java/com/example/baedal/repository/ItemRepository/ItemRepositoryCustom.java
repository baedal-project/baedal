package com.example.baedal.repository.ItemRepository;

import com.example.baedal.domain.Item;
import com.example.baedal.dto.response.ItemResponseDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    Optional<List<ItemResponseDto>> findByStoreId(Long storeId);

}
