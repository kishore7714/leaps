package com.leaps.dto.surrender;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderPolicyDetailsDto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long companyId;
	private String companyName;
	private Long clntNum;
	private Long policyNo;
	private String uinNumber;
	private String billFreq;
	private Double installmentPremium;
	private Double extraPremium;
	private String fup;
	private String docDate;
	private Integer laAge;
	private Integer phAge;
	private String statusCode;
	private String smokerFlag;
}
