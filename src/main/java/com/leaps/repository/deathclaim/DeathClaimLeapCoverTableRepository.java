package com.leaps.repository.deathclaim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.deathclaim.DeathClaimLeapCoverTable;

public interface DeathClaimLeapCoverTableRepository extends JpaRepository<DeathClaimLeapCoverTable, Long>{
	
	@Query(value = "select * from death_claim_leap_cover_table where valid_flag=1", nativeQuery = true)
    List<DeathClaimLeapCoverTable> getallActive();

    @Query(value = "select * from death_claim_leap_cover_table where id=:id and valid_flag=1", nativeQuery = true)
    Optional<DeathClaimLeapCoverTable> getActiveById(Long id);

    @Query(value = "select * from death_claim_leap_cover_table where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<DeathClaimLeapCoverTable> globalSearch(String key);

    @Query(value = "select * from death_claim_leap_cover_table where valid_flag=1", nativeQuery = true)
    Page<DeathClaimLeapCoverTable> getallActivePagination(Pageable pageable);
}
