package com.leaps.dto.surrender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderLeapDetailsDto {
	private Long id;
	private String companyName;
	private Long policyNo;
	private Long transNo;
	private String uinNumber;
	private String reqDate;
	private String logDate;
	private Double noOfDues;
	private Double totalPremium;
	private Double valueOfBonus;
	private Double cvbFactor;
	private Double gsvFactor;
	private Double gsvGross;
	private Double sbPaid;
	private Double gsvNet;
	private Double paidUpValue;
	private Double reversionaryBonus;
	private Double interimBonus;
	private Double guaranteedBonus;
	private Double terminalBonus;
	private Double ssvGrossAmount;
	private Double ssvFactor;
	private Double ssvNet;
	private String ssvOrGsv;
	private Double fundValue;
	private Double effDate;
	private Double policyDeposit;
	private Double penalInterest;
	private Double grossPay;
	private Double cdaCharge;
	private Double ulipSurrenderCharge;
	private Double totalRecovery;
	private Double tds;
	private Double netPayable;
	private Double policyLoan;
	private Double loanInterest;
	private Double cashValueBonus;
	private String makerFlag;
	private String checkerFlag;
	private String pfFlag;
	private String pfRemarks;
	private String leapApprovalFlag;
	private String leapApprovalRemarks;
	private String leapApprovalDate;
	private Long approvalQcUserId;
}
