package com.leaps.dto.deathclaim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimCoverDetailsDto {
	private Long id;
	private Long clntNum;
	private Long companyId;
	private String companyName;
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
