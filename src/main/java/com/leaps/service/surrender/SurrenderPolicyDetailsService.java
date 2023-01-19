package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderPolicyDetailsDto;
import com.leaps.responses.surrender.SurrenderPolicyDetailsResponse;

public interface SurrenderPolicyDetailsService {
	public List<SurrenderPolicyDetailsDto> getall();

	public SurrenderPolicyDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderPolicyDetailsDto getbyid(Long id);

	public String add(SurrenderPolicyDetailsDto dto);

	public String update(Long id, SurrenderPolicyDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderPolicyDetailsDto> globalSearch(String key);
}
