package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortalityRatesDto {
	private Long id;
	private Long companyId;
	private String companyName;
	private String plan;
	private String planName;
	private String uinNumber;
	private Double premTerm;
	private Integer age;
	private Double rates;
	private String startDate;
	private String endDate;
	private String gender;
	private String smoker;
}
