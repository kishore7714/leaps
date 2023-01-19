package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcLeapDetailsDto;
import com.leaps.responses.flc.FlcLeapDetailsResponse;

public interface FlcLeapDetailsService {
	
	public List<FlcLeapDetailsDto> getall();
	
	public FlcLeapDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public FlcLeapDetailsDto getbyid(Long id);

    public String add(FlcLeapDetailsDto dto);

    public String update(Long id, FlcLeapDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<FlcLeapDetailsDto> globalSearch(String key);
}
