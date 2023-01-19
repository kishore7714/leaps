package com.leaps.service.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimLeapCoverTableDto;
import com.leaps.responses.deathclaim.DeathClaimLeapCoverTableResponse;

public interface DeathClaimLeapCoverTableService {
	
	public List<DeathClaimLeapCoverTableDto> getall();

    public DeathClaimLeapCoverTableResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public DeathClaimLeapCoverTableDto getbyid(Long id);

    public String add(DeathClaimLeapCoverTableDto dto);

    public String update(Long id, DeathClaimLeapCoverTableDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<DeathClaimLeapCoverTableDto> globalSearch(String key);
}
