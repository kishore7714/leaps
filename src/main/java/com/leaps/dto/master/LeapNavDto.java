package com.leaps.dto.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeapNavDto {

private Long id;
	
	private Long companyId;
	private String companyName;
	
	private String fundCode;
	private String fundName;
	private String navDate;
	private Double rate;
}
