package com.example.baedal.repository;

import com.example.baedal.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAll();

    @Modifying //cache clear
    @Query(value = "UPDATE Store s SET s.avgStar = :finalStar WHERE s.id = :id")
    void updateAvgStar(@Param("finalStar") double finalStar, @Param("id") Long id);

    List<Store> findByNameContainsAndAddressContains(String name, String address);
}
