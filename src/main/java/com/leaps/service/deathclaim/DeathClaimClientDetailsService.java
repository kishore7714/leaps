package com.leaps.service.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimClientDetailsDto;
import com.leaps.responses.deathclaim.DeathClaimClientDetailsResponse;

public interface DeathClaimClientDetailsService {
	
	public List<DeathClaimClientDetailsDto> getall();

    public DeathClaimClientDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public DeathClaimClientDetailsDto getbyid(Long id);

    public String add(DeathClaimClientDetailsDto dto);

    public String update(Long id, DeathClaimClientDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<DeathClaimClientDetailsDto> globalSearch(String key);

}
