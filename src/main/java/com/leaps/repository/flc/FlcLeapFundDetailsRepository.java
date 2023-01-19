package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcLeapFundDetails;

public interface FlcLeapFundDetailsRepository extends JpaRepository<FlcLeapFundDetails, Long>{
	@Query(value = "select * from flc_leap_fund_details where valid_flag=1", nativeQuery = true)
    List<FlcLeapFundDetails> getallActive();

    @Query(value = "select * from flc_leap_fund_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcLeapFundDetails> getActiveById(Long id);

    @Query(value = "select * from flc_leap_fund_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcLeapFundDetails> globalSearch(String key);

    @Query(value = "select * from flc_leap_fund_details where valid_flag=1", nativeQuery = true)
    Page<FlcLeapFundDetails> getallActivePagination(Pageable pageable);
}
