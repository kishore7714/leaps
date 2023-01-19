package com.leaps.service.deathclaim;

import java.util.List;

import com.leaps.dto.deathclaim.DeathClaimCoverTablePasDto;
import com.leaps.responses.deathclaim.DeathClaimCoverTablePasResponse;

public interface DeathClaimCoverTablePasService {
	public List<DeathClaimCoverTablePasDto> getall();

    public DeathClaimCoverTablePasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public DeathClaimCoverTablePasDto getbyid(Long id);

    public String add(DeathClaimCoverTablePasDto dto);

    public String update(Long id, DeathClaimCoverTablePasDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<DeathClaimCoverTablePasDto> globalSearch(String key);
}
