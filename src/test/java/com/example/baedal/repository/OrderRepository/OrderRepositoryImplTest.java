//package com.example.baedal.repository.OrderRepository;

//import com.example.baedal.domain.OrderHasItem;


//@SpringBootTest
//@Transactional
//@DisplayName("order 관련 test")
//class OrderRepositoryImplTest {

//    @PersistenceContext
//    EntityManager em;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    JPAQueryFactory queryFactory;
//
//    @BeforeEach
//    public void before(){
//        queryFactory = new JPAQueryFactory(em);
//    }

//    @Test
//    @DisplayName("전체 주문개수 맞나 확인")
//    public void getAllOrder(){
//        //test cases들은 구현체와 최대한 결합도가 낮아야 good
//        //List<AllOrderResponseDto> allOrders = orderRepository.getAllOrder();
//        //List<OrderHasItem> allOrders = orderRepository.getAllOrder();
//        //List<Orders> allOrders = orderRepository.getAllOrder();
//        //assertThat(allOrders.size()).isEqualTo(12);
//    }

//    @Test
//    @DisplayName("주문 낱개로 잘 갖고 오나 확인")
//    public void getOneOrder(){
//        Orders oneOrder = orderRepository.getOneOrder(1L);
        //src 내용이랑 너무 의존성이 강함
//        assertThat(oneOrder.getAmount().equals(5));
//        assertThat(oneOrder.get(0).getStoreId().equals(1L));
//        assertThat(oneOrder.get(0).getStoreName().equals("명동왕족"));
//        assertThat(oneOrder.get(0).getItemName().equals("족발"));
//        assertThat(oneOrder.get(0).getMemberId().equals(1L));
//        assertThat(oneOrder.get(0).getMemberName().equals("yeongmin1"));

//    }



//}