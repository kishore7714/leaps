package com.leaps.repository.surrender;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.surrender.SurrenderFundDetailsPas;

public interface SurrenderFundDetailsPasRepository extends JpaRepository<SurrenderFundDetailsPas, Long>{
	@Query(value = "select * from surrender_fund_details_pas where valid_flag=1", nativeQuery = true)
    List<SurrenderFundDetailsPas> getallActive();

    @Query(value = "select * from surrender_fund_details_pas where id=:id and valid_flag=1", nativeQuery = true)
    Optional<SurrenderFundDetailsPas> getActiveById(Long id);

    @Query(value = "select * from surrender_fund_details_pas where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<SurrenderFundDetailsPas> globalSearch(String key);

	@Query(value = "select * from surrender_fund_details_pas where valid_flag=1", nativeQuery = true)
    Page<SurrenderFundDetailsPas> getallActivePagination(Pageable pageable);
}
