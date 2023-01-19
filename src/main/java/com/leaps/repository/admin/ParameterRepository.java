package com.leaps.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.admin.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    @Query(value = "select * from parameter_table where id like %:key% or short_description like %:key%or long_description like %:key% or rule like %:key%", nativeQuery = true)
    List<Parameter> globalSearch(String key);

}
