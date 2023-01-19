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

	private String policyNo;
	private String cntType;
	private String crTable;
	private String uinNumber;
	private String riskComDate;
	private String docDate;
	private Double sumAssured;
	private String premCessTerm;
	private String coverPremium;
	private String coverStatus;
}
