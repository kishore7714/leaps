package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GsvFactorDto {

	private Long id;

	private Long companyId;

	private String companyName;

	private String uinNumber;

	private String planName;

	private String planCode;

	private Double policyTerm;

	private Double premiumTerm;

	private String premiumType;

	private Double policyDuration;

	private Double rate;

	private String startDate;

	private String endDate;
}
