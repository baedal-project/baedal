package com.example.baedal.repository;


import com.example.baedal.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemId(Long id);
}
