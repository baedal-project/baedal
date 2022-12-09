package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.Member;
import com.example.baedal.domain.Store;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
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

    public ServiceCustom() {
    }

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

    //create Entity
    protected Store createStore(String name, String address, String category, Long memberId) {
        Store store = Store.builder()
                .name(name)
                .address(address)
                .category(category)
                .member(memberRepository.findByMemberId(memberId).get())
                .build();
        return store;
    }

    protected Item createItem(String name, int amount, int price, String category, Long storeId) {
            Item item = Item.builder()
                    .name(name)
                    .amount(amount)
                    .store(storeRepository.findByStoreId(storeId).get())
                    .price(price)
                    .category(category)
                    .build();

            return item;

        }

    protected Member createMember(String name, String nickname, String password, String address, String role) {
        Member member = Member.builder()
                .name(name)
                .nickname(nickname)
                .password(password)
                .address(address)
                .role(role)
                .build();
        return member;
    }

    //create Dto
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


}
