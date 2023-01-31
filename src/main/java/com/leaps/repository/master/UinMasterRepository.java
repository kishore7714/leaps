package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.UinMaster;

public interface UinMasterRepository extends JpaRepository<UinMaster, Long>{
	@Query(value = "select * from uin_master where valid_flag=1", nativeQuery = true)
    List<UinMaster> getallActive();

    @Query(value = "select * from uin_master where id=:id and valid_flag=1", nativeQuery = true)
    Optional<UinMaster> getActiveById(Long id);

    @Query(value = "select * from uin_master where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<UinMaster> globalSearch(String key);

    @Query(value = "select * from uin_master where valid_flag=1", nativeQuery = true)
    Page<UinMaster> getallActivePagination(Pageable pageable);
    
	@Query(value = "select * from uin_master where uin_number=:uinNo and valid_flag=1", nativeQuery = true)
	UinMaster getActiveByUIN(String uinNo);
}
