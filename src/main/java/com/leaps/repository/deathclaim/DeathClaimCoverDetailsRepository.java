package com.leaps.repository.deathclaim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.deathclaim.DeathClaimCoverDetails;

public interface DeathClaimCoverDetailsRepository extends JpaRepository<DeathClaimCoverDetails, Long>{
	@Query(value = "select * from death_claim_cover_details where valid_flag=1", nativeQuery = true)
    List<DeathClaimCoverDetails> getallActive();

    @Query(value = "select * from death_claim_cover_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<DeathClaimCoverDetails> getActiveById(Long id);

    @Query(value = "select * from death_claim_cover_details where id like %:key% and valid_flag = 1 or clnt_num like %:key% and valid_flag = 1", nativeQuery = true)
    List<DeathClaimCoverDetails> globalSearch(String key);

    @Query(value = "select * from death_claim_cover_details where valid_flag=1", nativeQuery = true)
    Page<DeathClaimCoverDetails> getallActivePagination(Pageable pageable);
}
