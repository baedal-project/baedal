package com.example.baedal.repository.StoreRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.example.baedal.domain.QStore.*;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public String getStoreName(Long storeId){

        return queryFactory
                .select(store.name)
                .from(store)
                .where(store.storeId.eq(storeId))
                .fetchOne();

    }

}
