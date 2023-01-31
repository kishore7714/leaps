package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcLeapDetails;

public interface FlcLeapDetailsRepository extends JpaRepository<FlcLeapDetails, Long>{

	@Query(value = "select * from flc_leap_details where valid_flag=1", nativeQuery = true)
    List<FlcLeapDetails> getallActive();

    @Query(value = "select * from flc_leap_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcLeapDetails> getActiveById(Long id);

    @Query(value = "select * from flc_leap_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcLeapDetails> globalSearch(String key);

    @Query(value = "select * from flc_leap_details where valid_flag=1", nativeQuery = true)
    Page<FlcLeapDetails> getallActivePagination(Pageable pageable);
    
    @Query(value = "select * from flc_leap_details where tran_no=:tranNo and valid_flag=1", nativeQuery = true)
    FlcLeapDetails getByTransNo(Long tranNo);
}
