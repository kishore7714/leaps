package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcTransactionDetails;

public interface FlcTransactionDetailsRepository extends JpaRepository<FlcTransactionDetails, Long>{

	@Query(value = "select * from flc_transaction_details where valid_flag=1", nativeQuery = true)
    List<FlcTransactionDetails> getallActive();

    @Query(value = "select * from flc_transaction_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcTransactionDetails> getActiveById(Long id);

    @Query(value = "select * from flc_transaction_details where id like %:key% and valid_flag = 1 or flc_policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcTransactionDetails> globalSearch(String key);
    
    //*********

    @Query(value = "select * from flc_transaction_details where valid_flag=1", nativeQuery = true)
    Page<FlcTransactionDetails> getallActivePagination(Pageable pageable);
    
    @Query(value = "select * from flc_transaction_details where flc_policy_no=:policyNo and interim_status = 'Initiated' and valid_flag=1", nativeQuery = true)
	FlcTransactionDetails findInitiatedByPolicyNo(Long policyNo);

	@Query(value = "select flc_policy_no from flc_transaction_details tp where interim_status = 'Initiated' and valid_flag =1;", nativeQuery = true)
	List<Long> findFlcPolicyNo();

	@Query(value = "select * from flc_transaction_details where flc_trans_no=:tran_no and valid_flag=1", nativeQuery = true)
	FlcTransactionDetails getByflctrano(Long tran_no);

	@Query(value = "select * from flc_transaction_details where flc_policy_no=:policyNo and interim_status = 'Processed' and valid_flag=1", nativeQuery = true)
	FlcTransactionDetails findProcessedByPolicyNo(Long policyNo);

	@Query(value ="select EXISTS(select * from flc_transaction_details where flc_trans_no=:flctransNo and valid_flag=1)", nativeQuery = true)
	Long existsByFlcTransNo(long flctransNo);
	
	@Query(value = "select * from flc_transaction_details where flc_trans_no=:tran_no and interim_status = 'Initiated' and valid_flag=1", nativeQuery = true)
	FlcTransactionDetails getInitiatedTransaction(Long tran_no);
	
	@Query(value = "select * from flc_transaction_details where flc_policy_no=:policyNo and valid_flag=1", nativeQuery = true)
	FlcTransactionDetails getByFlcPolicyNo(Long policyNo);
	
	@Query(value = "select * from flc_transaction_details where flc_policy_no=:policyNo and interim_status = 'QC Completed' and valid_flag=1", nativeQuery = true)
	FlcTransactionDetails findCompletedByPolicyNo(Long policyNo);
}
