package com.example.baedal.service;


import com.example.baedal.domain.Item;
import com.example.baedal.dto.response.ItemResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.CustomException;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.repository.ItemRepository.ItemRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    /*constructor DI - trouble Shooting
    * final 안붙이면 DI 안됨
    * 생성자 주입은 호출 시점에 1회 호출되는 것이 보장되기 때문에 주입받은 객체가 변하지 않음을 'final'을 통해 명시해준다.*/
    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllItems(Long storeId, HttpServletRequest request) {

        //case1) Refresh-Token이 Null일 때
        if (null == request.getHeader("Refresh-Token")) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        //case2) Authorization이 Null일 때
        if (null == request.getHeader("Authorization")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        //case3) storeId에 해당하는 store 없을 때 throw exception
        storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        //case4) storeId에 해당하는 item 없을 때 throw exception
        List<ItemResponseDto> itemLists = itemRepository.findByStoreId(storeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.STORE_NOT_FOUND.getMessage()));

        return ResponseDto.success(itemLists);
    }
}
