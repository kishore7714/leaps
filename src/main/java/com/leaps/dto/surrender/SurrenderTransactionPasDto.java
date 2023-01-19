package com.leaps.dto.surrender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderTransactionPasDto {
	private Long id;
	private Long companyId;
	private String companyName;
	private Long policyNo;
	private Long transNo;
	private String uinNumber;
	private String svReqDate;
	private String logDate;	
	private Double gsv;
	private Double ssv;
	private Double policyDeposit;
	private Double penalInterest;
	private Double grossPay;
	private Double cdaCharges;
	private Double tds;
	private Double cashValueBonus;
	private Double paidUpValue;
	private Double reversionaryBonus;
	private Double interimBonus;
	private Double totalRecovery;
	private Double otherRecovery;
	private Double netPayable;
	private String effectiveDate;
	private String approvDate;
	private Double fundValue;
	private Double policyLoan;
	private Double loanInterest;
	private Double gsvPayable;
	private Double totalBonus;
	private String makerFlag;
	private String checkerFlag;
	private String ipcaApprovalFlag;
	private String ipcaApprovalRemarks;
	private String ipcaApprovalDate;
	private Long qcUserId;
	private String interimStatus;
}
