package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderClientDetailsDto;
import com.leaps.responses.surrender.SurrenderClientDetailsResponse;

public interface SurrenderClientDetailsService {
	
	public List<SurrenderClientDetailsDto> getall();

	public SurrenderClientDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderClientDetailsDto getbyid(Long id);

	public String add(SurrenderClientDetailsDto dto);

	public String update(Long id, SurrenderClientDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderClientDetailsDto> globalSearch(String key);
}
