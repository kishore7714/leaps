package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GsvCashValueDto {

	private Long id;
	private Long companyId;

	private String companyName;
	
	private String uinNumber;
	private String planName;
	private String planCode;
	private Double yearsToMaturity;
	private Double cvbRate;
	private String startDate;
	private String endDate;
}
