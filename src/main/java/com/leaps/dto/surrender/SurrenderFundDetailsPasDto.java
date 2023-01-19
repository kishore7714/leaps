package com.leaps.dto.surrender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderFundDetailsPasDto {
	
	private  Long companyId;
	
    private String companyName;
    
    private  Long policyNo;
    
    private String fundCode;
    
    private String fundName;
    
    private String navDate;
    
    private Double units;
    
    private Double rateApp;
    
    private Double value;
}
