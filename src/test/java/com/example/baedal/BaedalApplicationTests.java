package com.example.baedal;

import com.example.baedal.test.Hello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
class BaedalApplicationTests {

//    @Autowired
//    EntityManager em;

    @Test
    void contextLoads() {
//
//        Hello hello = new Hello();
//        em.persist(hello);
//
//        JPAQueryFactory query = new JPAQueryFactory(em);
//        QHello qHello = new QHello("h");
//
//        Hello result = query
//                .selectFrom(qHello)
//                .fetchOne();
//
//        assertThat(result).isEqualTo(hello);
//        assertThat(result.getId()).isEqualTo(hello.getId());
    }

}
