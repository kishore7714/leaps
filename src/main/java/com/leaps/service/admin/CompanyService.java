package com.leaps.service.admin;

import java.util.List;

import com.leaps.dto.admin.CompanyDto;
import com.leaps.responses.admin.CompanyResponse;

public interface CompanyService {

    public List<CompanyDto> getall();

    public CompanyResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public CompanyDto getbyid(Long id);

    public String add(CompanyDto dto);

    public String update(Long id, CompanyDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<CompanyDto> globalSearch(String key);

}
