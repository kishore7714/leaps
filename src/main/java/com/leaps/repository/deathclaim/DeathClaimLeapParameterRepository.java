package com.leaps.repository.deathclaim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.deathclaim.DeathClaimLeapParameter;

public interface DeathClaimLeapParameterRepository extends JpaRepository<DeathClaimLeapParameter, Long>{
	
	@Query(value = "select * from death_claim_leap_parameter where valid_flag=1", nativeQuery = true)
    List<DeathClaimLeapParameter> getallActive();

    @Query(value = "select * from death_claim_leap_parameter where id=:id and valid_flag=1", nativeQuery = true)
    Optional<DeathClaimLeapParameter> getActiveById(Long id);

    @Query(value = "select * from death_claim_leap_parameter where id like %:key% and valid_flag = 1 or basic_sa like %:key% and valid_flag = 1", nativeQuery = true)
    List<DeathClaimLeapParameter> globalSearch(String key);

    @Query(value = "select * from death_claim_leap_parameter where valid_flag=1", nativeQuery = true)
    Page<DeathClaimLeapParameter> getallActivePagination(Pageable pageable);
}
