package com.leaps.service.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimCoverDetailsDto;
import com.leaps.responses.deathclaim.DeathClaimCoverDetailsResponse;

public interface DeathClaimCoverDetailsService {
	public List<DeathClaimCoverDetailsDto> getall();

    public DeathClaimCoverDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public DeathClaimCoverDetailsDto getbyid(Long id);

    public String add(DeathClaimCoverDetailsDto dto);

    public String update(Long id, DeathClaimCoverDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<DeathClaimCoverDetailsDto> globalSearch(String key);
}
