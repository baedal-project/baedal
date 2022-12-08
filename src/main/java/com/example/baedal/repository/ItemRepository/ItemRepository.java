package com.example.baedal.repository.ItemRepository;


import com.example.baedal.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    Optional<Item> findByItemId(Long id);

    //item Entity에서 amount에 해당하는 값만 가져오기

    @Query(value = "SELECT i.amount FROM Item i WHERE i.itemId = :id")
    Long findAmountByItemId(@Param("id") Long id);

}
