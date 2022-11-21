package com.example.baedal.dto.request;

import com.example.baedal.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String name;
    private String homeAddress;
    private String homeDetail;
    private String companyAddress;
    private String companyDetail;

}
