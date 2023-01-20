package com.leaps.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDto {
    private Long id;
//    private Long companyId;
//    private String companyName;
    private String userGroupName;
}
