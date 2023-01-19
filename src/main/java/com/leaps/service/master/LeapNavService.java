package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.LeapNavDto;
import com.leaps.responses.master.LeapNavResponse;

public interface LeapNavService {

	public List<LeapNavDto> getall();

    public LeapNavResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public LeapNavDto getbyid(Long id);

    public String add(LeapNavDto dto);

    public String update(Long id, LeapNavDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<LeapNavDto> globalSearch(String key);
}
