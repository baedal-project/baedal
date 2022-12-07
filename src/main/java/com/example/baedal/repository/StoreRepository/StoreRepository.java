package com.example.baedal.repository.StoreRepository;

import com.example.baedal.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    List<Store> findAll();

    @Modifying //cache clear
    @Query(value = "UPDATE Store s SET s.avgStar = :finalStar WHERE s.storeId = :id")
    void updateAvgStar(@Param("finalStar") double finalStar, @Param("id") Long id);

    List<Store> findByNameContainsAndAddressContains(String name, String address);

    Optional<Store> findByStoreId(Long storeId);

}
