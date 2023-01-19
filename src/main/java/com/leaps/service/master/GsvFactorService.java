package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.GsvFactorDto;
import com.leaps.responses.master.GsvFactorResponse;

public interface GsvFactorService {

	public List<GsvFactorDto> getall();

    public GsvFactorResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public GsvFactorDto getbyid(Long id);

    public String add(GsvFactorDto dto);

    public String update(Long id, GsvFactorDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<GsvFactorDto> globalSearch(String key);
}
