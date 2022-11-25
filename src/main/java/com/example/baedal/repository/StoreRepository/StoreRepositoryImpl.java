package com.example.baedal.repository.StoreRepository;

import com.example.baedal.domain.Store;
import com.example.baedal.dto.response.QStoreResponseDto;
import com.example.baedal.dto.response.StoreResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.baedal.domain.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreResponseDto> getAllStore(Pageable pageable) {
        List<StoreResponseDto> storeResponseDtoList = queryFactory
                .select(new QStoreResponseDto(store.storeId,
                        store.name,
                        store.address,
                        store.category))
                .from(store)
                .orderBy(store.storeId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery=queryFactory
                .select(store.count())
                .from(store);

        return PageableExecutionUtils.getPage(storeResponseDtoList, pageable,
                countQuery::fetchOne);
    }



}
