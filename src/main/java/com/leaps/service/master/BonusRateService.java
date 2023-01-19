package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.BonusRateDto;
import com.leaps.responses.master.BonusRateResponse;

public interface BonusRateService {
	public List<BonusRateDto> getall();

    public BonusRateResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public BonusRateDto getbyid(Long id);

    public String add(BonusRateDto dto);

    public String update(Long id, BonusRateDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<BonusRateDto> globalSearch(String key);
}
