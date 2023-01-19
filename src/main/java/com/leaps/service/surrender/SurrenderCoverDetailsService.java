package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderCoverDetailsDto;
import com.leaps.responses.surrender.SurrenderCoverDetailsResponse;

public interface SurrenderCoverDetailsService {
	
	public List<SurrenderCoverDetailsDto> getall();

	public SurrenderCoverDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderCoverDetailsDto getbyid(Long id);

	public String add(SurrenderCoverDetailsDto dto);

	public String update(Long id, SurrenderCoverDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderCoverDetailsDto> globalSearch(String key);
}
