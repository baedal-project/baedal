package com.example.baedal;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.Store;
import com.example.baedal.repository.ItemRepository;
import com.example.baedal.repository.StoreRepository;
import com.example.baedal.shared.Category;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class BaedalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaedalApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demo(StoreRepository storeRepository) {
//        return (args) -> {
//            storeRepository.save(Store.builder().name("명동왕족").address("jecheon-si").category("JOCKBAL").build());
//            storeRepository.save(Store.builder().name("오향족발").address("jecheon-si").category("JOCKBAL").build());
//            storeRepository.save(Store.builder().name("족황상제").address("jecheon-si").category("JOCKBAL").build());
//
//
//
//
//        };
//    }
    
//    @Bean
//    public CommandLineRunner demo(ItemRepository itemRepository) {
//        return (args) -> {
//            //storeRepository.save(new Store(1,"족발집","jecheon-si",Category.JOCKBAL,"족발집"));
//            //itemRepository.save(Item.builder().price(1500).category(Category.JOCKBAL).name("치킨").build());
//            itemRepository.save(Item.builder().price(1500).category(Category.JOCKBAL).name("족발").store(Store.builder().storeId(1L).build()).build());
//            itemRepository.save(Item.builder().price(1500).category(Category.JOCKBAL).name("족발").store(Store.builder().storeId(1L).build()).build());
//            itemRepository.save(Item.builder().price(2500).category(Category.JOCKBAL).name("족발").store(Store.builder().storeId(2L).build()).build());
//            itemRepository.save(Item.builder().price(3000).category(Category.JOCKBAL).name("족발").store(Store.builder().storeId(2L).build()).build());
//
//        };
//    }

//    @Bean
//    public CommandLineRunner demo(OrderRepository orderRepository) {
//        return (args) -> {
//            //storeRepository.save(new Store(1,"족발집","jecheon-si",Category.JOCKBAL,"족발집"));
//            //itemRepository.save(Item.builder().price(1500).category(Category.JOCKBAL).name("족발").build());
//            orderRepository.save(Orders.builder().member(Member.builder().Id(1L).build()).build());
//
//        };
//    }

//    @Bean
//    public CommandLineRunner demo() {
//        return (args) -> {
//            //storeRepository.save(new Store(1,"족발집","jecheon-si",Category.JOCKBAL,"족발집"));
//            //itemRepository.save(Item.builder().price(1500).category(Category.JOCKBAL).name("족발").build());
//
//
//        };
//    }

}
