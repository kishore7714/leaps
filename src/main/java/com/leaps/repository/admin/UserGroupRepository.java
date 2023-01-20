package com.leaps.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.admin.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query(value = "select * from user_group where valid_flag=1", nativeQuery = true)
    List<UserGroup> getallActive();

    @Query(value = "select * from user_group where id=:id and valid_flag=1", nativeQuery = true)
    Optional<UserGroup> getActiveById(Long id);

    @Query(value = "select * from user_group where id like %:key% and valid_flag = 1 or user_group_name like %:key% and valid_flag = 1", nativeQuery = true)
    List<UserGroup> globalSearch(String key);

    @Query(value = "select * from user_group where valid_flag=1", nativeQuery = true)
    Page<UserGroup> getallActivePagination(Pageable pageable);
    
    Optional<UserGroup> findByUserGroupName(String userGroupName);
}
