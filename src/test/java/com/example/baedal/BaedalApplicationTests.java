package com.example.baedal;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class BaedalApplicationTests {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

    //tdd as live template
//    @Test
//    public void () throws Exception {
//        //given
//
//        //when
//        //then
//     }


}
