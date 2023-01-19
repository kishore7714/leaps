package com.leaps.repository.surrender;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.surrender.SurrenderPolicyDetails;

public interface SurrenderPolicyDetailsRepository extends JpaRepository<SurrenderPolicyDetails, Long> {
	@Query(value = "select * from surrender_policy_details where valid_flag=1", nativeQuery = true)
	List<SurrenderPolicyDetails> getallActive();

	@Query(value = "select * from surrender_policy_details where id=:id and valid_flag=1", nativeQuery = true)
	Optional<SurrenderPolicyDetails> getActiveById(Long id);

	@Query(value = "select * from surrender_policy_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
	List<SurrenderPolicyDetails> globalSearch(String key);

	@Query(value = "select * from surrender_policy_details where valid_flag=1", nativeQuery = true)
	Page<SurrenderPolicyDetails> getallActivePagination(Pageable pageable);
}
