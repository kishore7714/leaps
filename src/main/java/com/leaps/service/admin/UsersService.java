package com.leaps.service.admin;

import java.util.List;

import com.leaps.dto.admin.UsersDto;
import com.leaps.responses.admin.UsersResponse;

public interface UsersService {

    public List<UsersDto> getall();

    public UsersResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public UsersDto getbyid(Long id);

    public String add(UsersDto dto);

    public String update(Long id, UsersDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<UsersDto> globalSearch(String key);

}
