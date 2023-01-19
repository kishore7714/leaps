package com.leaps.dto.surrender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderLeapFundDetailsDto {
	private Long id;
	private Long companyId;
	private String companyName;
	private Long policyNo;
	private String leapFundCode;
	private String leapFundName;
	private String leapNavDate;
	private Double leapUnits;
	private Double leapRateApp;
	private Double leapFundValue;
	private String status;
	private String remark;
}
