package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StamDutyMasterDto {

	private Long id;
	private Long companyId;
	private String companyName;
	private String uinNumber;
	private String coverName;
	private Double sdRate;
	private Float gstRate;
	private String startDate;
	private String endDate;
}
