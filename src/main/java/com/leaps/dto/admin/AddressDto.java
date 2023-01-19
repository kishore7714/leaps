package com.leaps.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

	private Long id;
	private String addressType;
	private String postalCode;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String address5;
	private String district;
	private String state;
	private String country;
	private String mobile;

}
