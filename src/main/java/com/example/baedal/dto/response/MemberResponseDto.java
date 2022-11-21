package com.example.baedal.dto.response;

import com.example.baedal.domain.Address;
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

//    private String homeAddress;
//    private String homeDetail;
//    private String companyAddress;
//    private String companyDetail;
    private Address homeAddress;
    private Address companyAddress;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
