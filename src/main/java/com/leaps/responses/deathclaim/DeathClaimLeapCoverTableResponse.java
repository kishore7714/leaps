package com.leaps.responses.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimLeapCoverTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimLeapCoverTableResponse {
	
	private List<DeathClaimLeapCoverTableDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
