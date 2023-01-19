package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.StamDutyMasterDto;
import com.leaps.responses.master.StamDutyMasterResponse;

public interface StamDutyMasterService {
	
	public List<StamDutyMasterDto> getall();

	public StamDutyMasterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public StamDutyMasterDto getbyid(Long id);

	public String add(StamDutyMasterDto dto);

	public String update(Long id, StamDutyMasterDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<StamDutyMasterDto> globalSearch(String key);
}
