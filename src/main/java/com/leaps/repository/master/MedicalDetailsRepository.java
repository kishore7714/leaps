package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.MedicalDetails;

public interface MedicalDetailsRepository extends JpaRepository<MedicalDetails, Long>{
	@Query(value = "select * from medical_details where valid_flag=1", nativeQuery = true)
    List<MedicalDetails> getallActive();

    @Query(value = "select * from medical_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<MedicalDetails> getActiveById(Long id);

    @Query(value = "select * from medical_details where id like %:key% and valid_flag = 1 or fund_code like %:key% and valid_flag = 1", nativeQuery = true)
    List<MedicalDetails> globalSearch(String key);

    @Query(value = "select * from medical_details where valid_flag=1", nativeQuery = true)
    Page<MedicalDetails> getallActivePagination(Pageable pageable);
    
	@Query(value = "select mf_rate from medical_details where medical_category=:category and medical_center=:center and tpa_code=:tpaCpde and valid_flag=1", nativeQuery = true)
	Double getMfRate(String category,String center,String tpaCpde);
}
