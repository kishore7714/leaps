package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsvFactorDto {
	
	private Long id;

	private Long companyId;
	
	private String companyName;

	private String uinNumber;

	private String planName;

	private String planCode;

	private String premiumType;

	private Double premiumTerm;

	private Double policyTerm;

	private Double policyDuration;

	private Double rate;

	private String startDate;

	private String endDate;
}
