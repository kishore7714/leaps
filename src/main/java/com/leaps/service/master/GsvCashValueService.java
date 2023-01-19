package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.GsvCashValueDto;
import com.leaps.responses.master.GsvCashValueResponse;

public interface GsvCashValueService {
	public List<GsvCashValueDto> getall();

    public GsvCashValueResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public GsvCashValueDto getbyid(Long id);

    public String add(GsvCashValueDto dto);

    public String update(Long id, GsvCashValueDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<GsvCashValueDto> globalSearch(String key);
}
