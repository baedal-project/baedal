package com.example.baedal.repository.ItemRepository;

import com.example.baedal.dto.response.ItemResponseDto;
import com.example.baedal.dto.response.QItemResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.baedal.domain.QItem.*;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<ItemResponseDto>> findByStoreId(Long storeId){
        //storeId에 해당하는 items를 찾아서 반환
        return Optional.ofNullable(queryFactory.select(new QItemResponseDto(item.itemId, item.name, item.price, item.category))
                .from(item)
                .where(item.store.storeId.eq(storeId))
                .fetch());

    }
}
