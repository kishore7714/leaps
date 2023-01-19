package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortalityFlagMasterDto {
	private Long id;
	private Long companyId;
	private String companyName;

	private String uinNumber;
	private String coverName;
	private String mortFlag;
	private Float gstRate;
	private String startDate;
	private String endDate;
}
