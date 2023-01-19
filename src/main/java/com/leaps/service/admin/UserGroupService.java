package com.leaps.service.admin;

import java.util.List;

import com.leaps.dto.admin.UserGroupDto;
import com.leaps.responses.admin.UserGroupResponse;

public interface UserGroupService {
    public List<UserGroupDto> getall();

    public UserGroupResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public UserGroupDto getbyid(Long id);

    public String add(UserGroupDto dto);

    public String update(Long id, UserGroupDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<UserGroupDto> globalSearch(String key);
}
