package com.leaps.dto.admin;

import com.leaps.entity.admin.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private Long id;
    private Long addressId;
    private Address address;
    private String companyCode;
    private String companyName;
    private String companyShortName;
    private String companyLongName;
    private String gst;

    private String cin;

    private String cinDate;

    private String tin;

    private String pan;

    private String companyLogo;

}
