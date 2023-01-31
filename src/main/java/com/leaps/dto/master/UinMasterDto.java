package com.leaps.dto.master;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UinMasterDto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long companyId;
	private String companyName;
	private String uinNumber;
	private String planName;
	private String planCode;
	private String gsvFactor;
	private String gsvCashValue;
	private String ssvFactor;
	private String productType;
	private String flcEligibility;
	private Double surrenderChargeRate;
	private String startDate;
	private String endDate;
}
