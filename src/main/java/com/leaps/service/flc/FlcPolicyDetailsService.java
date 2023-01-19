package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcPolicyDetailsDto;
import com.leaps.responses.flc.FlcPolicyDetailsResponse;

public interface FlcPolicyDetailsService {
	public List<FlcPolicyDetailsDto> getall();

	public FlcPolicyDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public FlcPolicyDetailsDto getbyid(Long id);

	public String add(FlcPolicyDetailsDto dto);

	public String update(Long id, FlcPolicyDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<FlcPolicyDetailsDto> globalSearch(String key);
}
