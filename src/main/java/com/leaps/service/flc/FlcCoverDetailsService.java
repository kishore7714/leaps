package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcCoverDetailsDto;
import com.leaps.responses.flc.FlcCoverDetailsResponse;

public interface FlcCoverDetailsService {
	public List<FlcCoverDetailsDto> getall();

    public FlcCoverDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public FlcCoverDetailsDto getbyid(Long id);

    public String add(FlcCoverDetailsDto dto);

    public String update(Long id, FlcCoverDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<FlcCoverDetailsDto> globalSearch(String key);
}
