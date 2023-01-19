package com.leaps.dto.surrender;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderClientDetailsDto {

	private Long id;
	private Long companyId;
	private String companyName;
	
	private Long clntNum;
	private String laName;
	private String laDob;
	private String nationality;
	private String residentStatus;
	private String gender;
	private String contactNumber;
	@Email(message = "Please Enter a Valid Email")
	private String emailId;
}
