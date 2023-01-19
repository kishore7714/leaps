package com.leaps.responses.flc;

import java.util.List;

import com.leaps.dto.flc.FlcCoverDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcCoverDetailsResponse {
	
	private List<FlcCoverDetailsDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
