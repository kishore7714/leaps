package com.leaps.dto.flc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcLeapDetailsDto {

	private Long id;

	private Long companyId;
	private String companyName;

	private String tranDate;
	private Long flcPolicyNo;
	private Long tranNo;
	private String uinNumber;
	private Double totalPremium;
	private Double avalSuspense;
	private Double penalInterest;
	private Double medicalFee;
	private Double stampDuty;
	private Double mortCharge;
	private Double grossPayable;
	private Double recoveries;
	private Double netPayable;
	private String effDate;
	private Double fundValue;
	private String pfFlag;
	private String pfRemarks;
	private String approvFlag;
	private String approvalDate;
	private String approvRemarks;
	private String pfFlagUpdate;

}
