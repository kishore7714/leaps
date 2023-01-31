package com.leaps.dto.flc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcCoverDetailsDto {
	private Long id;

	private Long companyId;
	private String companyName;

	private Long clntNum;

	private Long policyNo;
	private String planName;
	private String planCode;
	private String uinNumber;
	private String riskComDate;
	private String docDate;
	private Double sumAssured;
	private Double premCessTerm;
	private Double coverPremium;
	private String coverStatus;
}
