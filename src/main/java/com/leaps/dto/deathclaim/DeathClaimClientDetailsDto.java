package com.leaps.dto.deathclaim;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimClientDetailsDto {
	
	private Long id;
	private Long clntNum;
	private Long companyId;
	private String companyName;
	private Long contactNumber;
	@Email(message = "Please Enter a Valid Email")
	@NotEmpty(message = "Email Should not be Empty")
	private String emailId;
	private String laName;
	private String laDob;
	private String nationality;
	private String residentStatus;
	private String gender;
	
	

}
