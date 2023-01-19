package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcLeapFundDetailsDto;
import com.leaps.responses.flc.FlcLeapFundDetailsResponse;

public interface FlcLeapFundDetailsService {

	public List<FlcLeapFundDetailsDto> getall();

	public FlcLeapFundDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public FlcLeapFundDetailsDto getbyid(Long id);

	public String add(FlcLeapFundDetailsDto dto);

	public String update(Long id, FlcLeapFundDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<FlcLeapFundDetailsDto> globalSearch(String key);
}
