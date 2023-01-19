package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonusRateDto {
	
	private Long id;
	
	private Long companyId;
	
	private String companyName;
	
	private String uinNumber;
	
	private String planName;
	
	private String planCode;
	
	private String financialYear;
	
	private Double bonusRate;
	
	private String bonusType;
	
	private String startDate;
	
	private String endDate;
}
