package com.leaps.dto.flc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcTransactionDetailsDto {

	private Long id;
	private Long companyId;
	private String companyName;

	private Long flcPolicyNo;
	private Long flcTransNo;
	private String flcReqDate;
	private String flcLogDate;
	private String uinNumber;
	private Long flcPremRefund; // non ulip policy
	private Long flcTotalPrem; // ulip policy
	private Long flcPolicyDop;
	private Long penalIntrest;
	private Double grossFlcPay;
	private Long medicalFee;
	private Double stamDuty;
	private Double riskPremRecov; // non ulip policy
	private Double mortChargeRefund; // ulip policy
	private Double totalRecov;
	private Double netFlcPay;
	private String effDate; // ulip policy
	private Double fundValue; // ulip policy
	private String flcApprovalDate;
	private String medicalCategory;
	private String medicalCenter;
	private String medicatTpaCode;
	private String makerFlag;
	private String checkerFlag;
	private String leapApprovalFlag;
	private String leapApprovalRemark;
	private String leapApprovalDate;
	private Long approvalQcUserId;
	private String interimStatus;
}
