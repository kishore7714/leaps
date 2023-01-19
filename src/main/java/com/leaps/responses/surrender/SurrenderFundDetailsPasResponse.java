package com.leaps.responses.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderFundDetailsPasDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderFundDetailsPasResponse {
	private List<SurrenderFundDetailsPasDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
