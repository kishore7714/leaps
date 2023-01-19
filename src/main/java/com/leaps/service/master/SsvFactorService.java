package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.SsvFactorDto;
import com.leaps.responses.master.SsvFactorResponse;

public interface SsvFactorService {

	public List<SsvFactorDto> getall();

	public SsvFactorResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SsvFactorDto getbyid(Long id);

	public String add(SsvFactorDto dto);

	public String update(Long id, SsvFactorDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SsvFactorDto> globalSearch(String key);
}
