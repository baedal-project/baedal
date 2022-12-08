package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.dto.request.MemberRequestDto;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.jwt.TokenProvider;
import com.example.baedal.repository.ItemRepository.ItemRepository;
import com.example.baedal.repository.LikeRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.OrderHasItemRepository;
import com.example.baedal.repository.OrderRepository.OrderRepository;
import com.example.baedal.repository.RefreshTokenRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@SpringBootTest
public class ServiceCustom{

    @Autowired
    MemberService memberService;

    @Autowired
    OrderService orderService;

    @Autowired
    LikeService likeService;

    @Autowired
    SearchService searchService;

    @Autowired
    StoreService storeService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderHasItemRepository orderHasItemRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    public void beforeEach() {
        //FK check 후 삭제
        orderHasItemRepository.deleteAll();
        likeRepository.deleteAll();
        storeRepository.deleteAll();
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    public void afterEach() {
        //FK check 후 삭제
        orderHasItemRepository.deleteAll();
        likeRepository.deleteAll();
        storeRepository.deleteAll();
        itemRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        orderRepository.deleteAll();
        memberRepository.deleteAll();
    }

    protected MemberRequestDto createMemberRequestDto(String name, String nickname, String password, String address, String role) {
        return MemberRequestDto.builder()
                .name(name)
                .nickname(nickname)
                .password(password)
                .address(address)
                .role(role)
                .build();
    }

    protected OrderRequestDto createOrderRequestDto(Long storeId, Long item1, Integer amount1, Long memberId) {

        List<Long> itemId = new ArrayList<>();
        itemId.add(item1);

        List<Integer> amount = new ArrayList<>();
        amount.add(amount1);

        return OrderRequestDto.builder()
                .storeId(storeId)
                .itemId(itemId)
                .amount(amount)
                .memberId(memberId)
                .build();
    }

    //    @PersistenceContext
//    EntityManager em;

//    @AfterEach
//    @DisplayName("auto_increment reset")
//    public void teardown() {
//        //DB Reset
//        this.memberRepository.deleteAll();
//        this.em
//            .createNativeQuery("ALTER TABLE member AUTO_INCREMENT=1")
//            .executeUpdate();
//
//    }



}
