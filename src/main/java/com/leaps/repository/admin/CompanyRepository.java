package com.leaps.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.admin.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query(value = "select * from company where valid_flag=1", nativeQuery = true)
    List<Company> getallActive();

    @Query(value = "select * from company where id=:id and valid_flag=1", nativeQuery = true)
    Optional<Company> getActiveById(Long id);

    @Query(value = "select * from company where id like %:key% and valid_flag = 1 or company_code like %:key% and valid_flag = 1 or company_long_name like %:key%  and valid_flag = 1 or company_short_name like %:key%  and valid_flag = 1 or cin like %:key%  and valid_flag = 1 or cin_date like %:key% and valid_flag = 1 or gst like %:key% and valid_flag = 1 or pan like %:key% and valid_flag = 1 or tin like %:key% and valid_flag = 1", nativeQuery = true)
    List<Company> globalSearch(String key);

    @Query(value = "select * from company where valid_flag=1", nativeQuery = true)
    Page<Company> getallActivePagination(Pageable pageable);
}
