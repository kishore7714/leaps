package com.leaps.dto.deathclaim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimCoverTablePasDto {
	
	private Long id;
	private Long companyId;
	private String companyName;
	private String uinNumber;
	private String doc;
	private String riskComDate;
	private Double originalSumAssured;
	private Double pasSumAssured;
	private String flag;
	private String remark;
}
