package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcFundDetailsPas;

public interface FlcFundDetailsPasRepository extends JpaRepository<FlcFundDetailsPas, Long>{
	
	@Query(value = "select * from flc_fund_details_pas where valid_flag=1", nativeQuery = true)
    List<FlcFundDetailsPas> getallActive();

    @Query(value = "select * from flc_fund_details_pas where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcFundDetailsPas> getActiveById(Long id);

    @Query(value = "select * from flc_fund_details_pas where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcFundDetailsPas> globalSearch(String key);

    @Query(value = "select * from flc_fund_details_pas where valid_flag=1", nativeQuery = true)
    Page<FlcFundDetailsPas> getallActivePagination(Pageable pageable);
    
	@Query(value = "select * from flc_fund_details_pas where policy_no=:policyNo and valid_flag=1", nativeQuery = true)
	List<FlcFundDetailsPas> getallByPolicy(Long policyNo);


}
