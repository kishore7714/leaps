package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.UinMasterDto;
import com.leaps.responses.master.UinMasterResponse;

public interface UinMasterService {
	
	public List<UinMasterDto> getall();

	public UinMasterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public UinMasterDto getbyid(Long id);

	public String add(UinMasterDto dto);

	public String update(Long id, UinMasterDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<UinMasterDto> globalSearch(String key);
}
