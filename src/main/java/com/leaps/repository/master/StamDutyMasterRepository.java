package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.StamDutyMaster;

public interface StamDutyMasterRepository extends JpaRepository<StamDutyMaster, Long>{

	@Query(value = "select * from stam_duty_master where valid_flag=1", nativeQuery = true)
    List<StamDutyMaster> getallActive();

    @Query(value = "select * from stam_duty_master where id=:id and valid_flag=1", nativeQuery = true)
    Optional<StamDutyMaster> getActiveById(Long id);

    @Query(value = "select * from stam_duty_master where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<StamDutyMaster> globalSearch(String key);

    @Query(value = "select * from stam_duty_master where valid_flag=1", nativeQuery = true)
    Page<StamDutyMaster> getallActivePagination(Pageable pageable);
    
    @Query(value = "select * from stam_duty_master where uin_number=:uinNumber and valid_flag=1", nativeQuery = true)
	StamDutyMaster getByUniqueNo(String uinNumber);
}
