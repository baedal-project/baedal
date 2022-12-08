package com.example.baedal.repository.StoreRepository;

import com.example.baedal.domain.Store;
import org.springframework.data.domain.Pageable;
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

    //List<Store> findByNameContainsAndAddressContains(String name, String address);

    Optional<Store> findByStoreId(Long storeId);

    /*v1) table full scan 사용*/
    @Query(value = "SELECT s FROM Store s WHERE s.address LIKE %:address%")
    List<Store> findByAddressV1(@Param("address") String address, Pageable pageable);

    /*v2) DB indexing/full-text search 사용*/
    /*alter table store add fulltext (address); MySQL에서 설정 필요*/
    @Query(value = "SELECT * FROM Store s WHERE MATCH(s.address) AGAINST((?1) IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Store> findByAddressV2(@Param("address") String address, Pageable pageable);

    /*v2) DB indexing/full-text search 사용 페이징X*/
    /*alter table store add fulltext (address); MySQL에서 설정 필요*/
    @Query(value = "SELECT * FROM Store s WHERE MATCH(s.address) AGAINST((?1) IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Store> findByAddressV2(@Param("address") String address);
}
