package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderLeapDetailsDto;
import com.leaps.responses.surrender.SurrenderLeapDetailsResponse;

public interface SurrenderLeapDetailsService {
	
	public List<SurrenderLeapDetailsDto> getall();

	public SurrenderLeapDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderLeapDetailsDto getbyid(Long id);

	public String add(SurrenderLeapDetailsDto dto);

	public String update(Long id, SurrenderLeapDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderLeapDetailsDto> globalSearch(String key);
}
