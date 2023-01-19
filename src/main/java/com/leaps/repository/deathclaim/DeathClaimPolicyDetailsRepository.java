package com.leaps.repository.deathclaim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.deathclaim.DeathClaimPolicyDetails;

public interface DeathClaimPolicyDetailsRepository extends JpaRepository<DeathClaimPolicyDetails, Long> {
	
	@Query(value = "select * from death_claim_policy_details where valid_flag=1", nativeQuery = true)
    List<DeathClaimPolicyDetails> getallActive();

    @Query(value = "select * from death_claim_policy_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<DeathClaimPolicyDetails> getActiveById(Long id);

    @Query(value = "select * from death_claim_policy_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<DeathClaimPolicyDetails> globalSearch(String key);

    @Query(value = "select * from death_claim_policy_details where valid_flag=1", nativeQuery = true)
    Page<DeathClaimPolicyDetails> getallActivePagination(Pageable pageable);
}
