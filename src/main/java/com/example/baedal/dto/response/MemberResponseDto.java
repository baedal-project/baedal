package com.example.baedal.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long Id;
    private String name;
    private String nickname;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public MemberResponseDto(Long Id, String name, String nickname, String address){
        this.Id = Id;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
    }

}
