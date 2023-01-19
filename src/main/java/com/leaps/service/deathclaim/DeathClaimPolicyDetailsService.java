package com.leaps.service.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimPolicyDetailsDto;
import com.leaps.responses.deathclaim.DeathClaimPolicyDetailsResponse;

public interface DeathClaimPolicyDetailsService {
	
	public List<DeathClaimPolicyDetailsDto> getall();

    public DeathClaimPolicyDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public DeathClaimPolicyDetailsDto getbyid(Long id);

    public String add(DeathClaimPolicyDetailsDto dto);

    public String update(Long id, DeathClaimPolicyDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<DeathClaimPolicyDetailsDto> globalSearch(String key);
}
