package com.leaps.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTableDto {

	private Long id;
	private String serviceName;
	private String servicePurpose;
}
