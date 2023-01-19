package com.leaps.responses.master;

import java.util.List;

import com.leaps.dto.master.MortalityRatesDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortalityRatesResponse {
	private List<MortalityRatesDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
