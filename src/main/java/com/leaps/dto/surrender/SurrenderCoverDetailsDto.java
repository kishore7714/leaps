package com.leaps.dto.surrender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderCoverDetailsDto {
	private Long id;
	private Long companyId;

	private String companyName;

	private Long clntNum;
	private Long policyNo;
	private String uinNumber;
	private String planName;
	private String planCode;
	private String riskComDate;
	private String docDate;
	private Double sumAssured;
	private Double premiumTerm; // prem cess term
	private Double policyTerm; // policy term / risk cess term
	private Double coverPremium;
	private String coverStatus;
}
