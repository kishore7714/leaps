package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcFundDetailsPasDto;
import com.leaps.responses.flc.FlcFundDetailsPasResponse;

public interface FlcFundDetailsPasService {
	
	public List<FlcFundDetailsPasDto> getall();

	public FlcFundDetailsPasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public FlcFundDetailsPasDto getbyid(Long id);

	public String add(FlcFundDetailsPasDto dto);

	public String update(Long id, FlcFundDetailsPasDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<FlcFundDetailsPasDto> globalSearch(String key);
}
