package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.MortalityFlagMasterDto;
import com.leaps.responses.master.MortalityFlagMasterResponse;

public interface MortalityFlagMasterService {

	public List<MortalityFlagMasterDto> getall();

	public MortalityFlagMasterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public MortalityFlagMasterDto getbyid(Long id);

	public String add(MortalityFlagMasterDto dto);

	public String update(Long id, MortalityFlagMasterDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<MortalityFlagMasterDto> globalSearch(String key);
}
