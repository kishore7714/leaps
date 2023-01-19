package com.leaps.responses.master;

import java.util.List;

import com.leaps.dto.master.GsvCashValueDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GsvCashValueResponse {
	private List<GsvCashValueDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
