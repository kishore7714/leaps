package com.leaps.dto.deathclaim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimLeapParameterDto {
	
	private Long id;

	private Long companyId;

	private String companyName;

	private String basicSa;
	private Long increaseSaYears;
	private Long percentageSaIncrease;
	private String reversionaryBonus;
	private String loyaltyBonus;
	private String guaranteedBonus;
	private String terminalBonus;
	private String suicideClause;
	private Long waitingPeriod;
	private String refundOfAdminFee;
	private String refundOfMc;
	private String refundOfGuaranteedCharges;
	private String returnOfPremiums;
	private String fundvalueSaPayable;
	private String claimConcession;
	private String tdsType;
	private Long tdsRate;
}
