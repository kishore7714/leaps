package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderFundDetailsPasDto;
import com.leaps.responses.surrender.SurrenderFundDetailsPasResponse;

public interface SurrenderFundDetailsPasService {
	
	public List<SurrenderFundDetailsPasDto> getall();

	public SurrenderFundDetailsPasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderFundDetailsPasDto getbyid(Long id);

	public String add(SurrenderFundDetailsPasDto dto);

	public String update(Long id, SurrenderFundDetailsPasDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderFundDetailsPasDto> globalSearch(String key);
}
