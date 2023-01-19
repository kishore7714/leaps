package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.BonusRate;

public interface BonusRateRepository extends JpaRepository<BonusRate, Long>{
	
	@Query(value = "select * from bonus_rate where valid_flag=1", nativeQuery = true)
    List<BonusRate> getallActive();

    @Query(value = "select * from bonus_rate where id=:id and valid_flag=1", nativeQuery = true)
    Optional<BonusRate> getActiveById(Long id);

    @Query(value = "select * from bonus_rate where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1 or plan_name like %:key%  and valid_flag = 1 or plan_code like %:key%  and valid_flag = 1", nativeQuery = true)
    List<BonusRate> globalSearch(String key);

    @Query(value = "select * from bonus_rate where valid_flag=1", nativeQuery = true)
    Page<BonusRate> getallActivePagination(Pageable pageable);

}
