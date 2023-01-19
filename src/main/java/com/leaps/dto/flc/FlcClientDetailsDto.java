package com.leaps.dto.flc;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcClientDetailsDto {
	
	private Long id;

	private Long companyId;
	private String companyName;

	private Long clntNum;
	private String laName;
	private String laDob;
	private String residentStatus;
	private String nationality;
	private String gender;
	private String contactNumber;
	
	@Email(message = "Please Enter a Valid E-mail")
	private String emailId;
}
