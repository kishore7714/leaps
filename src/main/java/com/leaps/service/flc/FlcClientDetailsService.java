package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcClientDetailsDto;
import com.leaps.responses.flc.FlcClientDetailsResponse;

public interface FlcClientDetailsService {
	
	public List<FlcClientDetailsDto> getall();

    public FlcClientDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public FlcClientDetailsDto getbyid(Long id);

    public String add(FlcClientDetailsDto dto);

    public String update(Long id, FlcClientDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<FlcClientDetailsDto> globalSearch(String key);
}
