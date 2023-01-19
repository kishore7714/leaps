package com.leaps.repository.surrender;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.surrender.SurrenderLeapFundDetails;

public interface SurrenderLeapFundDetailsRepository extends JpaRepository<SurrenderLeapFundDetails, Long>{
	@Query(value = "select * from surrender_leap_fund_details where valid_flag=1", nativeQuery = true)
    List<SurrenderLeapFundDetails> getallActive();

    @Query(value = "select * from surrender_leap_fund_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<SurrenderLeapFundDetails> getActiveById(Long id);

    @Query(value = "select * from surrender_leap_fund_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<SurrenderLeapFundDetails> globalSearch(String key);

	@Query(value = "select * from surrender_leap_fund_details where valid_flag=1", nativeQuery = true)
    Page<SurrenderLeapFundDetails> getallActivePagination(Pageable pageable);
}
