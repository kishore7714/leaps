package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.MortalityRatesDto;
import com.leaps.responses.master.MortalityRatesResponse;

public interface MortalityRatesService {
	
	public List<MortalityRatesDto> getall();

	public MortalityRatesResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public MortalityRatesDto getbyid(Long id);

	public String add(MortalityRatesDto dto);

	public String update(Long id, MortalityRatesDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<MortalityRatesDto> globalSearch(String key);
}
