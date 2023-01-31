package com.leaps.dto.flc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcPolicyDetailsDto {
	private Long id;

	private Long companyId;
	private String companyName;
	private Long clntNum;
	private Long policyNo;
	private String uinNumber;
	private String billFreq;
	private Double installmentPremium;
	private String premToDate;
	private String docDate;
	private Integer laAge;
	private Integer phAge;
	private String statCode;
	private String medicalFlag;
	private String smokerFlag;
}
