package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.MortalityRates;

public interface MortalityRatesRepository extends JpaRepository<MortalityRates, Long>{
	@Query(value = "select * from mortality_rates where valid_flag=1", nativeQuery = true)
    List<MortalityRates> getallActive();

    @Query(value = "select * from mortality_rates where id=:id and valid_flag=1", nativeQuery = true)
    Optional<MortalityRates> getActiveById(Long id);

    @Query(value = "select * from mortality_rates where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<MortalityRates> globalSearch(String key);

    @Query(value = "select * from mortality_rates where valid_flag=1", nativeQuery = true)
    Page<MortalityRates> getallActivePagination(Pageable pageable);
    
    @Query(value="select rates from mortality_rates where uin_number=:uinNumber  and age=:age and prem_term=:premTerm and valid_flag=1",nativeQuery = true)
	Float findByCCCT(String uinNumber, Integer age,Double premTerm);
}
