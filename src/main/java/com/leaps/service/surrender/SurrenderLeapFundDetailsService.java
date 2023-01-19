package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderLeapFundDetailsDto;
import com.leaps.responses.surrender.SurrenderLeapFundDetailsResponse;

public interface SurrenderLeapFundDetailsService {
	public List<SurrenderLeapFundDetailsDto> getall();

	public SurrenderLeapFundDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderLeapFundDetailsDto getbyid(Long id);

	public String add(SurrenderLeapFundDetailsDto dto);

	public String update(Long id, SurrenderLeapFundDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderLeapFundDetailsDto> globalSearch(String key);
}
