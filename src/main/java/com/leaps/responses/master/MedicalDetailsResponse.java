package com.leaps.responses.master;

import java.util.List;

import com.leaps.dto.master.MedicalDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalDetailsResponse {

	private List<MedicalDetailsDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last; 
}
