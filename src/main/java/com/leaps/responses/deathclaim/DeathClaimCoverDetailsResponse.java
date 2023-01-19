package com.leaps.responses.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimCoverDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeathClaimCoverDetailsResponse {
	private List<DeathClaimCoverDetailsDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
