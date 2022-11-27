package com.example.baedal.service;


import com.example.baedal.domain.Likes;
import com.example.baedal.domain.Store;
import com.example.baedal.dto.request.LikeRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.LikeRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Random;


@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    int max = 5;
    int min = 0;
    double initStar = 0.0;

    @Transactional
    public ResponseDto<?> postLike(LikeRequestDto requestDto) {

        //random number generator
        //returns a value in the range [0,5]
        Random random = new Random();
        int star = random.nextInt((max - min)+1) + min;
        double dstar = star;
        //double star = Math.random() * (max-min) + min;
        //System.out.println("평점 잘 데려오니" + star);
        //System.out.println("평점 잘 데려오니" + dstar);

        Store store = storeRepository.findByStoreId(requestDto.getStoreId()).orElse(null);
        //System.out.println("가게 잘 찾아오니" + store.getName());

        Likes likes = Likes.builder()
                .member(memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null))
                .store(store)
                .star(star)
                .build();

        likeRepository.save(likes);

        //store에 평균점수 집어넣기
        //System.out.println(storeRepository.findById(requestDto.getStoreId()));
        int count = likeRepository.countByStore_StoreId(requestDto.getStoreId());
        double dcount = count;
        //System.out.println("특정 가게에 대한 평점 잘 나오니" + count);
        initStar = store.getAvgStar() * (count-1);
        //System.out.println("원래 평점 잘 나오니" + initStar);
        double finalStar = initStar + dstar;
        //System.out.println("추가된 총점 잘 나오니" + finalStar);
        double avgStar = Double.parseDouble(String.format("%.2f", finalStar/dcount));
        //System.out.println("평균점수 잘 나오니" + avgStar);

        storeRepository.updateAvgStar(avgStar, requestDto.getStoreId());

        return ResponseDto.success(star);

    }

}
