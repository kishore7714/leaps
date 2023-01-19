package com.leaps.responses.admin;

import java.util.List;

import com.leaps.dto.admin.UserGroupDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupResponse {

    private List<UserGroupDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
