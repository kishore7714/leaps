package com.leaps.repository.flc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.flc.FlcClientDetails;

public interface FlcClientDetailsRepository extends JpaRepository<FlcClientDetails, Long>{

	@Query(value = "select * from flc_client_details where valid_flag=1", nativeQuery = true)
    List<FlcClientDetails> getallActive();

    @Query(value = "select * from flc_client_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<FlcClientDetails> getActiveById(Long id);

    @Query(value = "select * from flc_client_details where id like %:key% and valid_flag = 1 or clnt_num like %:key% and valid_flag = 1", nativeQuery = true)
    List<FlcClientDetails> globalSearch(String key);

    @Query(value = "select * from flc_client_details where valid_flag=1", nativeQuery = true)
    Page<FlcClientDetails> getallActivePagination(Pageable pageable);
    
	@Query(value = "select * from flc_client_details where clnt_num=:clientNo and valid_flag=1", nativeQuery = true)
	FlcClientDetails getActiveByClientNo(Long clientNo);
}
