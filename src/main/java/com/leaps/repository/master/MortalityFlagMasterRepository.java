package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.MortalityFlagMaster;

public interface MortalityFlagMasterRepository extends JpaRepository<MortalityFlagMaster, Long>{
	@Query(value = "select * from mortality_flag_master where valid_flag=1", nativeQuery = true)
    List<MortalityFlagMaster> getallActive();

    @Query(value = "select * from mortality_flag_master where id=:id and valid_flag=1", nativeQuery = true)
    Optional<MortalityFlagMaster> getActiveById(Long id);

    @Query(value = "select * from mortality_flag_master where id like %:key% and valid_flag = 1 or fund_code like %:key% and valid_flag = 1", nativeQuery = true)
    List<MortalityFlagMaster> globalSearch(String key);

    @Query(value = "select * from mortality_flag_master where valid_flag=1", nativeQuery = true)
    Page<MortalityFlagMaster> getallActivePagination(Pageable pageable);
    
	@Query(value = "select * from mortality_flag_master where uin_number=:uinNumber and valid_flag=1", nativeQuery = true)
	MortalityFlagMaster getByUniqueNo(String uinNumber);
}
