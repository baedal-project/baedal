package com.example.baedal;


import com.example.baedal.domain.Item;
import com.example.baedal.domain.Member;
import com.example.baedal.domain.Store;
import com.example.baedal.repository.ItemRepository.ItemRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableJpaAuditing
public class BaedalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaedalApplication.class, args);

    }
//    @Bean
//    public CommandLineRunner demo (MemberRepository memberRepository, PasswordEncoder encoder){
//        return (args) -> {
//            memberRepository.save(Member.builder().name("name1").address("address1").role("CONSUMER").password(encoder.encode("1234")).nickname("nickname1").build());
//            memberRepository.save(Member.builder().name("name2").address("address2").role("PRODUCER").password(encoder.encode("1234")).nickname("nickname2").build());
//
//        };
//    }

//    @Bean
//    public CommandLineRunner demo (StoreRepository storeRepository){
//        return (args) -> {
//            storeRepository.save(Store.builder().name("store1").address("address1").category("category1").member(Member.builder().memberId(2L).build()).build());
//            storeRepository.save(Store.builder().name("store2").address("address2").category("category2").member(Member.builder().memberId(2L).build()).build());
//            storeRepository.save(Store.builder().name("store3").address("address3").category("category3").member(Member.builder().memberId(2L).build()).build());
//
//
//        };
//    }


//    @Bean
//    public CommandLineRunner demo(ItemRepository itemRepository) {
//        return (args) -> {
//            itemRepository.save(Item.builder().price(1500).category("category1").name("name1").amount(30).store(Store.builder().storeId(1L).build()).build());
//            itemRepository.save(Item.builder().price(1500).category("category2").name("name1").amount(30).store(Store.builder().storeId(1L).build()).build());
//            itemRepository.save(Item.builder().price(1500).category("category3").name("name2").amount(40).store(Store.builder().storeId(2L).build()).build());
//            itemRepository.save(Item.builder().price(1500).category("category4").name("name2").amount(40).store(Store.builder().storeId(2L).build()).build());
//            itemRepository.save(Item.builder().price(1500).category("category5").name("name3").amount(40).store(Store.builder().storeId(3L).build()).build());
//            itemRepository.save(Item.builder().price(1500).category("category6").name("name4").amount(40).store(Store.builder().storeId(3L).build()).build());
//
//        };
//    }
}
