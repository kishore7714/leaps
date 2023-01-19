package com.leaps.dto.deathclaim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimPolicyDetailsDto {
	
	private Long id;

	private Long companyId;
	private String companyName;

	private Long clntNum;
	private Long chdrNum;
	private String uinNumber;
	private String billFreq;
	private Double installmentPremium;
	private Double extraPremium;
	private String fup;
	private String docDate;
	private Integer laAge;
	private Integer phAge;
	private String statusCode;
	private String smokerFlag;
}
