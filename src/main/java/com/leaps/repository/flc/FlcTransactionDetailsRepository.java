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

    @Query(value = "select * from flc_transaction_details where valid_flag=1", nativeQuery = true)
    Page<FlcTransactionDetails> getallActivePagination(Pageable pageable);
}
