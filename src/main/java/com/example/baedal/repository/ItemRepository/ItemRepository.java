package com.example.baedal.repository.ItemRepository;


import com.example.baedal.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    /*pessimistic lock - select for update
    * v1) 다른 transaction에서 읽기와 쓰기 모두 불가능*/
    //@Lock(LockModeType.PESSIMISTIC_WRITE)   //v1
    @Lock(LockModeType.PESSIMISTIC_WRITE)  //v2
    //@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    //@QueryHints({@QueryHint(name = "javax.persistence.lock.scope", value = "Extended")})
    Optional<Item> findByItemId(Long id);

    @Query(value = "update Item i set i.amount = i.amount - :amount where i.itemId = :itemId")
    @Modifying
    void updateItemAmount(@Param("itemId") Long itemId, @Param("amount") int amount);

    //item Entity에서 amount에 해당하는 값만 가져오기
    @Query(value = "SELECT i.amount FROM Item i WHERE i.itemId = :id")
    Integer findAmountByItemId(@Param("id") Long id);



}
