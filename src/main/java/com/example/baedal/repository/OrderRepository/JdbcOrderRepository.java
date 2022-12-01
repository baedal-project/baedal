package com.example.baedal.repository.OrderRepository;


import com.example.baedal.domain.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository implements JdbcOrderRepositoryInterface{
    private final JdbcTemplate jdbcTemplate;
    private int batchSize = 5;
    //전체 데이터 batch size로 나눠서 Batch Insert
    @Override
    public void saveAll(List<Orders> ordersList){
        int batchCount = 0;
        List<Orders> subOrdersLists = new ArrayList<>();
        for (int i = 0; i < ordersList.size(); i++) {
            subOrdersLists.add(ordersList.get(i));
            if ((i + 1) % batchSize == 0) {
                System.out.println("test1");
                batchCount = batchInsertOrders(batchCount, subOrdersLists);
            }
        }
        if (!subOrdersLists.isEmpty()){
            System.out.println("test2");
            batchCount = batchInsertOrders(batchCount, subOrdersLists);
        }
        System.out.println("batchCount: " + batchCount);

    }
//    @Override
//    @Transactional
//    public void saveAll(List<Orders> ordersList){
//        String sql = "INSERT INTO orders "
//                + "(created_at, modified_at, member_id, store_id) VALUES (?, ?, ?, ?)";
//        jdbcTemplate.batchUpdate(sql, ordersList, batchSize, (PreparedStatement ps, Orders orders) -> {
//            ps.setObject(1, LocalDateTime.now());
//            ps.setObject(2, LocalDateTime.now());
//            ps.setLong(3, orders.getMember().getMemberId());
//            ps.setLong(4, orders.getStore().getStoreId());
//        });
//    }



    //자투리 데이터 batchInsertOrders로 저장
    @Transactional
    public int batchInsertOrders(int batchCount, List<Orders> subOrdersLists){
        String sql = "INSERT INTO orders "
                + "(created_at, modified_at, member_id, store_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Orders orders = subOrdersLists.get(i);
                //ps.setLong(1, orders.getOrdersId());
                ps.setObject(1, LocalDateTime.now());
                ps.setObject(2, LocalDateTime.now());
                ps.setLong(3, orders.getMember().getMemberId());
                ps.setLong(4, orders.getStore().getStoreId());
            }

            @Override
            public int getBatchSize() {
                return subOrdersLists.size();
            }
        });

        subOrdersLists.forEach(orders -> orders.setOrdersId(
                                jdbcTemplate.queryForObject("select last_insert_id()", Long.class)
                        ));
        subOrdersLists.clear();
        batchCount++;
        return batchCount;
    }

}
