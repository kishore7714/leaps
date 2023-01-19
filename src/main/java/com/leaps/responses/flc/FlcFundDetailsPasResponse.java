package com.leaps.responses.flc;

import java.util.List;

import com.leaps.dto.flc.FlcFundDetailsPasDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcFundDetailsPasResponse {
	
	private List<FlcFundDetailsPasDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
