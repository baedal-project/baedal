package com.example.baedal.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {

    private String address;

    @Column(name = "address_detail")
    private String detail;  //상세주소

    public Address(String address, String detail){
        this.address = address;
        this.detail = detail;
    }

}
