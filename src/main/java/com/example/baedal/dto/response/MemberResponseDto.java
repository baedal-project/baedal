package com.example.baedal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long Id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
