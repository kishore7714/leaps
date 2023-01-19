package com.leaps.responses.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderCoverDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurrenderCoverDetailsResponse {
	private List<SurrenderCoverDetailsDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
