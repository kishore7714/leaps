package com.leaps.dto.flc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcLeapFundDetailsDto {
	private Long companyId;
	private String companyName;
	private Long policyNo;
	private String leapFundCode;
	private String leapFundName;
	private String leapNavDate;
	private Double leapRateApp;
	private Double leapUnits;
	private Double leapFundValue;
	private String status;
	private String remark;
}
