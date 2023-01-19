package com.leaps.dto.flc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcFundDetailsPasDto {
	private Long id;

	private Long companyId;
	private String companyName;

	private Long policyNo;
	private String fundCode;
	private String fundName;
	private Double rateApp;
	private Double units;
	private Double value;
	private String navDate;
}
