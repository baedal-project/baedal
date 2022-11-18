package com.example.baedal.repository;

import com.example.baedal.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikeRepository extends JpaRepository<Likes, Long> {

    //@Query(value = "select l from Likes l where l.storeId = ")
    int countByStoreId(Long storeId);
}
