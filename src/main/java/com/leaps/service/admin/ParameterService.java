package com.leaps.service.admin;

import java.util.List;

import com.leaps.dto.admin.ParameterDto;
import com.leaps.responses.admin.ParameterResponse;

public interface ParameterService {

    public List<ParameterDto> getall();

    public ParameterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public ParameterDto getbyid(Long id);

    public String add(ParameterDto dto);

    public String update(Long id, ParameterDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<ParameterDto> globalSearch(String key);

}
