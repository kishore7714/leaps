package com.leaps.repository.deathclaim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.deathclaim.DeathClaimClientDetails;

public interface DeathClaimClientDetailsRepository extends JpaRepository<DeathClaimClientDetails, Long>{
	
	@Query(value = "select * from death_claim_client_details where valid_flag=1", nativeQuery = true)
    List<DeathClaimClientDetails> getallActive();

    @Query(value = "select * from death_claim_client_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<DeathClaimClientDetails> getActiveById(Long id);

    @Query(value = "select * from death_claim_client_details where id like %:key% and valid_flag = 1 or clnt_num like %:key% and valid_flag = 1", nativeQuery = true)
    List<DeathClaimClientDetails> globalSearch(String key);

    @Query(value = "select * from death_claim_client_details where valid_flag=1", nativeQuery = true)
    Page<DeathClaimClientDetails> getallActivePagination(Pageable pageable);
}
