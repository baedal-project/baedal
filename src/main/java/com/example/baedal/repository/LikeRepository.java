package com.example.baedal.repository;

import com.example.baedal.domain.Likes;
import com.example.baedal.domain.Store;
import com.example.baedal.dto.response.MemberResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Likes, Long> {

    //@Query(value = "select l from Likes l where l.storeId = ")

    int countByStore_StoreId(Long storeId);


    Optional<Likes> findByMemberAndStore(Long memberId, Long storeId);
}
