package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcCoverDetails;

public interface FlcCoverDetailsRepository extends JpaRepository<FlcCoverDetails, Long>{
	
	@Query(value = "select * from flc_cover_details where valid_flag=1", nativeQuery = true)
    List<FlcCoverDetails> getallActive();

    @Query(value = "select * from flc_cover_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcCoverDetails> getActiveById(Long id);

    @Query(value = "select * from flc_cover_details where id like %:key% and valid_flag = 1 or clnt_num like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcCoverDetails> globalSearch(String key);

    @Query(value = "select * from flc_cover_details where valid_flag=1", nativeQuery = true)
    Page<FlcCoverDetails> getallActivePagination(Pageable pageable);
    
    @Query(value = "select * from flc_cover_details where policy_no=:policy_no and valid_flag=1", nativeQuery = true)
	List<FlcCoverDetails> getAllByPolicyNo(Long policy_no);
	
	@Query(value="select * from flc_cover_details where policy_no = :policyNo and valid_flag=1",nativeQuery = true)
	FlcCoverDetails findByPolicyNo(Long policyNo);
	
	@Query(value = "select * from flc_cover_details where policy_no=:policy and valid_flag=1", nativeQuery = true)
	FlcCoverDetails getByPolicy(Long policy);
}
