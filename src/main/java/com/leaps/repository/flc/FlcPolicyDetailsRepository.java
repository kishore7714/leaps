package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcPolicyDetails;

public interface FlcPolicyDetailsRepository extends JpaRepository<FlcPolicyDetails, Long>{
	@Query(value = "select * from flc_policy_details where valid_flag=1", nativeQuery = true)
    List<FlcPolicyDetails> getallActive();

    @Query(value = "select * from flc_policy_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcPolicyDetails> getActiveById(Long id);

    @Query(value = "select * from flc_policy_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcPolicyDetails> globalSearch(String key);

    @Query(value = "select * from flc_policy_details where valid_flag=1", nativeQuery = true)
    Page<FlcPolicyDetails> getallActivePagination(Pageable pageable);
    
    @Query(value = "select * from flc_policy_details where clnt_num=:clnt_num and valid_flag=1", nativeQuery = true)
	List<FlcPolicyDetails> getByClientNo(Long clnt_num);
	
	@Query(value = "select * from flc_policy_details where flc_policy_no=:flcPolicyNo and valid_flag=1", nativeQuery = true)
	FlcPolicyDetails getActiveByPolicyNo(Long flcPolicyNo);
	
	@Query(value="select anb_at_ccd from flc_policy_details where flc_policy_no = :policyNo and valid_flag=1",nativeQuery = true)
	Integer getByAge(Long policyNo);
	
	@Query(value="select EXISTS( select * from flc_policy_details where flc_policy_no = :policyNo and valid_flag=1)",nativeQuery = true)
	Long existsByFlcPolicyNo(Long policyNo);
}
