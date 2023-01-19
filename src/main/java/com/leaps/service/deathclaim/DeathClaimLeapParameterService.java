package com.leaps.service.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimLeapParameterDto;
import com.leaps.responses.deathclaim.DeathClaimLeapParameterResponse;

public interface DeathClaimLeapParameterService {
	public List<DeathClaimLeapParameterDto> getall();

    public DeathClaimLeapParameterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public DeathClaimLeapParameterDto getbyid(Long id);

    public String add(DeathClaimLeapParameterDto dto);

    public String update(Long id, DeathClaimLeapParameterDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<DeathClaimLeapParameterDto> globalSearch(String key);
}
