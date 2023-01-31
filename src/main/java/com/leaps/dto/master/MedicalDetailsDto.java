package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalDetailsDto {
	private Long id;
	private Long companyId;
	private String companyName;
	private String tpaCode;
	private String prodName;
	private String medicalCategory;
	private String medicalCenter;
	private Double mfRate;
	private String startDate;
	private String endDate;
}
